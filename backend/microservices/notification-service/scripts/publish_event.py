"""Publier un événement ApplicationSubmitted à la main, pour tester notification SANS candidature.

    python -m scripts.publish_event               # un événement valide
    python -m scripts.publish_event --twice       # le même événement deux fois (idempotence)
    python -m scripts.publish_event --invalid     # un message illisible (-> DLQ)
"""
import argparse
import asyncio
import json
import uuid
from datetime import date, datetime, timezone

import aio_pika

from app.config import settings
from app.messaging.consumer import EXCHANGE, ROUTING_KEY


def sample_event() -> dict:
    return {
        "eventId": str(uuid.uuid4()),
        "eventType": "ApplicationSubmitted",
        "occurredAt": datetime.now(timezone.utc).isoformat(),
        "applicationId": 6,
        "applicationDate": date.today().isoformat(),
        "candidateId": 4,
        "candidateFirstname": "Lina",
        "candidateLastname": "Mansour",
        "candidateEmail": "lina.mansour@example.com",
        "jobId": 1,
        "jobName": "Java Spring Boot Developer",
    }


async def main(twice: bool, invalid: bool) -> None:
    connection = await aio_pika.connect_robust(settings.rabbitmq_url)
    async with connection:
        channel = await connection.channel()
        exchange = await channel.declare_exchange(EXCHANGE, aio_pika.ExchangeType.TOPIC, durable=True)

        event = sample_event()
        body = b"{ ceci n'est pas du JSON" if invalid else json.dumps(event).encode()
        message = aio_pika.Message(
            body=body,
            content_type="application/json",
            delivery_mode=aio_pika.DeliveryMode.PERSISTENT,  # écrit sur disque par RabbitMQ
            message_id=event["eventId"],
        )
        for _ in range(2 if twice else 1):
            await exchange.publish(message, routing_key=ROUTING_KEY)
            print(f"Publié sur '{EXCHANGE}' avec la clé '{ROUTING_KEY}' (eventId={event['eventId']})")


if __name__ == "__main__":
    parser = argparse.ArgumentParser()
    parser.add_argument("--twice", action="store_true", help="publier deux fois le même événement")
    parser.add_argument("--invalid", action="store_true", help="publier un message illisible")
    args = parser.parse_args()
    asyncio.run(main(args.twice, args.invalid))
