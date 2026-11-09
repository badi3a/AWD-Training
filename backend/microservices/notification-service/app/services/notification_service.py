"""Logique métier des notifications (stockage en mémoire, pas de base de données)."""
import logging
from datetime import datetime, timezone
from typing import Optional

from app.schemas import (
    ApplicationSubmittedEvent,
    Notification,
    NotificationCreate,
    NotificationStatus,
    NotificationType,
)

log = logging.getLogger("notification.service")


class NotificationService:
    def __init__(self) -> None:
        self._notifications: list[Notification] = []
        self._next_id = 1
        # Idempotence : RabbitMQ garantit « au moins une fois », un même événement peut arriver deux fois.
        self._processed_event_ids: set[str] = set()

    # ---------- lecture ----------
    def find_all(self) -> list[Notification]:
        return list(self._notifications)

    def find_by_id(self, notification_id: int) -> Optional[Notification]:
        return next((n for n in self._notifications if n.id == notification_id), None)

    # ---------- réaction à un événement ----------
    def handle_application_submitted(self, event: ApplicationSubmittedEvent) -> Optional[Notification]:
        """Crée et « envoie » l'e-mail de confirmation. Retourne None si l'événement est un doublon."""
        if event.eventId in self._processed_event_ids:
            log.info("Événement %s déjà traité : ignoré (consommateur idempotent)", event.eventId)
            return None

        notification = self._create(
            NotificationCreate(
                recipient=event.candidateEmail,
                subject=f"Candidature reçue : {event.jobName}"[:150],
                message=(
                    f"Bonjour {event.candidateFirstname}, votre candidature n°{event.applicationId} "
                    f"au poste « {event.jobName} » a bien été reçue le "
                    f"{event.applicationDate:%d/%m/%Y}."
                ),
                type=NotificationType.EMAIL,
                candidateId=event.candidateId,
            ),
            event_id=event.eventId,
        )
        self._send(notification)
        self._processed_event_ids.add(event.eventId)
        return notification

    # ---------- interne ----------
    def _create(self, data: NotificationCreate, event_id: Optional[str] = None) -> Notification:
        notification = Notification(
            **data.model_dump(),
            id=self._next_id,
            createdAt=datetime.now(timezone.utc),
            eventId=event_id,
        )
        self._next_id += 1
        self._notifications.append(notification)
        return notification

    @staticmethod
    def _send(notification: Notification) -> None:
        # Simulation : un vrai service appellerait un serveur SMTP ou une API d'e-mail ici.
        log.info("E-mail envoyé à %s : %s", notification.recipient, notification.subject)
        notification.status = NotificationStatus.SENT


# Instance unique partagée par les routes et le consommateur RabbitMQ
notification_service = NotificationService()
