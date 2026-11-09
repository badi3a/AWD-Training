"""Tests de la logique métier : aucun broker nécessaire."""
from datetime import date, datetime, timezone

from app.schemas import ApplicationSubmittedEvent, NotificationStatus
from app.services.notification_service import NotificationService


def make_event(event_id: str = "evt-1") -> ApplicationSubmittedEvent:
    return ApplicationSubmittedEvent(
        eventId=event_id,
        eventType="ApplicationSubmitted",
        occurredAt=datetime(2026, 10, 18, 10, 0, tzinfo=timezone.utc),
        applicationId=6,
        applicationDate=date(2026, 10, 18),
        candidateId=4,
        candidateFirstname="Lina",
        candidateLastname="Mansour",
        candidateEmail="lina.mansour@example.com",
        jobId=1,
        jobName="Java Spring Boot Developer",
    )


def test_event_creates_a_sent_email():
    service = NotificationService()

    notification = service.handle_application_submitted(make_event())

    assert notification is not None
    assert notification.recipient == "lina.mansour@example.com"
    assert notification.status == NotificationStatus.SENT
    assert "Java Spring Boot Developer" in notification.subject
    assert notification.candidateId == 4
    assert notification.eventId == "evt-1"
    assert service.find_all() == [notification]


def test_same_event_twice_gives_one_notification():
    service = NotificationService()

    service.handle_application_submitted(make_event("evt-42"))
    duplicate = service.handle_application_submitted(make_event("evt-42"))

    assert duplicate is None
    assert len(service.find_all()) == 1


def test_two_different_events_give_two_notifications():
    service = NotificationService()

    service.handle_application_submitted(make_event("evt-1"))
    service.handle_application_submitted(make_event("evt-2"))

    assert [n.id for n in service.find_all()] == [1, 2]
