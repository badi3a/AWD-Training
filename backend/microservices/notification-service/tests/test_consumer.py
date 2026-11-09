"""Tests du consommateur : un faux message remplace RabbitMQ (ack / reject observés)."""
import asyncio
import json

from app.messaging.consumer import ApplicationSubmittedConsumer
from app.services.notification_service import NotificationService

VALID_BODY = {
    "eventId": "evt-7",
    "eventType": "ApplicationSubmitted",
    "occurredAt": "2026-10-18T10:00:00.123456Z",   # format Instant de Java
    "applicationId": 6,
    "applicationDate": "2026-10-18",                # format LocalDate de Java
    "candidateId": 4,
    "candidateFirstname": "Lina",
    "candidateLastname": "Mansour",
    "candidateEmail": "lina.mansour@example.com",
    "jobId": 1,
    "jobName": "Java Spring Boot Developer",
}


class FakeMessage:
    def __init__(self, body: bytes) -> None:
        self.body = body
        self.message_id = "test"
        self.acked = False
        self.rejected_with_requeue = None

    async def ack(self) -> None:
        self.acked = True

    async def reject(self, requeue: bool = False) -> None:
        self.rejected_with_requeue = requeue


def deliver(consumer: ApplicationSubmittedConsumer, body: bytes) -> FakeMessage:
    message = FakeMessage(body)
    asyncio.run(consumer.on_message(message))
    return message


def test_valid_message_is_acknowledged():
    service = NotificationService()
    consumer = ApplicationSubmittedConsumer("amqp://unused", service)

    message = deliver(consumer, json.dumps(VALID_BODY).encode())

    assert message.acked is True
    assert message.rejected_with_requeue is None
    assert len(service.find_all()) == 1


def test_unreadable_message_goes_to_dead_letter_queue():
    service = NotificationService()
    consumer = ApplicationSubmittedConsumer("amqp://unused", service)

    message = deliver(consumer, b"{ not json")

    assert message.acked is False
    assert message.rejected_with_requeue is False   # reject sans requeue -> DLX -> DLQ
    assert service.find_all() == []


def test_missing_field_goes_to_dead_letter_queue():
    service = NotificationService()
    consumer = ApplicationSubmittedConsumer("amqp://unused", service)
    body = dict(VALID_BODY)
    del body["candidateEmail"]

    message = deliver(consumer, json.dumps(body).encode())

    assert message.rejected_with_requeue is False


def test_redelivered_message_is_acknowledged_but_not_sent_twice():
    service = NotificationService()
    consumer = ApplicationSubmittedConsumer("amqp://unused", service)
    body = json.dumps(VALID_BODY).encode()

    first = deliver(consumer, body)
    second = deliver(consumer, body)

    assert first.acked and second.acked   # le doublon est acquitté (sinon il reviendrait sans fin)
    assert len(service.find_all()) == 1


def test_extra_field_from_producer_is_ignored():
    service = NotificationService()
    consumer = ApplicationSubmittedConsumer("amqp://unused", service)
    body = dict(VALID_BODY, newFieldAddedLater="ok")

    message = deliver(consumer, json.dumps(body).encode())

    assert message.acked is True
