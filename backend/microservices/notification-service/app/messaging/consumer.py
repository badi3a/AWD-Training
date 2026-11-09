"""Consommateur RabbitMQ de l'événement ApplicationSubmitted (bibliothèque aio-pika).

Topologie déclarée ici (le consommateur possède SA queue) :

    candidature --publish--> [exchange topic "jobboard.events"]
                                  | binding "application.submitted"
                                  v
                        [queue "notification.application-submitted"] --> ce consommateur
                                  | message rejeté (requeue=False)
                                  v
    [exchange direct "jobboard.events.dlx"] --> [queue "notification.application-submitted.dlq"]
"""
import asyncio
import contextlib
import logging
from typing import Optional

import aio_pika
from aio_pika.abc import AbstractChannel, AbstractIncomingMessage, AbstractQueue, AbstractRobustConnection

from app.schemas import ApplicationSubmittedEvent
from app.services.notification_service import NotificationService

log = logging.getLogger("notification.consumer")

EXCHANGE = "jobboard.events"                                   # déclaré aussi par candidature (mêmes paramètres)
ROUTING_KEY = "application.submitted"
QUEUE = "notification.application-submitted"                   # <service>.<événement>
DEAD_LETTER_EXCHANGE = "jobboard.events.dlx"
DEAD_LETTER_QUEUE = "notification.application-submitted.dlq"
PREFETCH = 10                                                  # au plus 10 messages non acquittés en même temps
RETRY_DELAY_SECONDS = 5


class ApplicationSubmittedConsumer:
    def __init__(self, url: str, service: NotificationService) -> None:
        self._url = url
        self._service = service
        self._connection: Optional[AbstractRobustConnection] = None
        self._task: Optional[asyncio.Task] = None

    # ---------- cycle de vie (appelé par le lifespan FastAPI) ----------
    def start(self) -> None:
        """Lance la consommation en tâche de fond : FastAPI démarre même si RabbitMQ est arrêté."""
        self._task = asyncio.create_task(self._run(), name="rabbitmq-consumer")

    async def stop(self) -> None:
        if self._task is not None:
            self._task.cancel()
            with contextlib.suppress(asyncio.CancelledError):
                await self._task
        if self._connection is not None and not self._connection.is_closed:
            await self._connection.close()
        log.info("Consommateur RabbitMQ arrêté")

    async def _run(self) -> None:
        # 1. Se connecter (connect_robust se reconnecte seul ensuite, mais pas au tout premier essai)
        while self._connection is None:
            try:
                self._connection = await aio_pika.connect_robust(self._url)
            except Exception as exc:  # broker arrêté, mauvais identifiants...
                log.warning("RabbitMQ injoignable (%s) : nouvel essai dans %s s", exc, RETRY_DELAY_SECONDS)
                await asyncio.sleep(RETRY_DELAY_SECONDS)

        # 2. Ouvrir un canal, limiter le nombre de messages en cours de traitement
        channel = await self._connection.channel()
        await channel.set_qos(prefetch_count=PREFETCH)

        # 3. Déclarer la topologie (opération idempotente) puis consommer
        queue = await self.declare_topology(channel)
        await queue.consume(self.on_message)  # no_ack=False : acquittement manuel
        log.info("En écoute sur la queue '%s' (binding '%s' sur '%s')", QUEUE, ROUTING_KEY, EXCHANGE)

    @staticmethod
    async def declare_topology(channel: AbstractChannel) -> AbstractQueue:
        exchange = await channel.declare_exchange(EXCHANGE, aio_pika.ExchangeType.TOPIC, durable=True)

        # Dead-letter : où vont les messages rejetés (message illisible, erreur de traitement)
        dlx = await channel.declare_exchange(DEAD_LETTER_EXCHANGE, aio_pika.ExchangeType.DIRECT, durable=True)
        dlq = await channel.declare_queue(DEAD_LETTER_QUEUE, durable=True)
        await dlq.bind(dlx, routing_key=QUEUE)

        queue = await channel.declare_queue(
            QUEUE,
            durable=True,  # la queue survit au redémarrage de RabbitMQ
            arguments={
                "x-dead-letter-exchange": DEAD_LETTER_EXCHANGE,
                "x-dead-letter-routing-key": QUEUE,
            },
        )
        await queue.bind(exchange, routing_key=ROUTING_KEY)
        return queue

    # ---------- traitement d'un message ----------
    async def on_message(self, message: AbstractIncomingMessage) -> None:
        """ack si le traitement réussit ; reject(requeue=False) sinon -> le message part en DLQ."""
        try:
            event = ApplicationSubmittedEvent.model_validate_json(message.body)
            self._service.handle_application_submitted(event)
        except Exception as exc:
            log.error("Message %s rejeté vers la DLQ : %s", message.message_id, exc)
            await message.reject(requeue=False)
            return
        await message.ack()
