"""Modèles Pydantic : validation des données + schémas Swagger."""
from datetime import date, datetime
from enum import Enum
from typing import Literal, Optional

from pydantic import BaseModel, ConfigDict, Field


class NotificationType(str, Enum):
    EMAIL = "EMAIL"
    SMS = "SMS"


class NotificationStatus(str, Enum):
    PENDING = "PENDING"
    SENT = "SENT"


class NotificationCreate(BaseModel):
    recipient: str = Field(..., min_length=1, examples=["badia@example.com"])
    subject: str = Field(..., min_length=1, max_length=150)
    message: str = Field(..., min_length=1)
    type: NotificationType = NotificationType.EMAIL
    candidateId: Optional[int] = None


class Notification(NotificationCreate):
    id: int
    status: NotificationStatus = NotificationStatus.PENDING
    createdAt: datetime
    # Chapitre 6 : identifiant de l'événement RabbitMQ qui a déclenché la notification
    eventId: Optional[str] = None


class ApplicationSubmittedEvent(BaseModel):
    """Contrat du message publié par candidature sur l'exchange "jobboard.events".

    Les noms de champs sont identiques aux clés JSON produites par le record Java
    ApplicationSubmittedEvent. Un champ manquant ou mal typé -> ValidationError -> DLQ.
    """

    model_config = ConfigDict(extra="ignore")  # un champ ajouté par le producteur ne casse rien

    eventId: str
    eventType: Literal["ApplicationSubmitted"]
    occurredAt: datetime
    applicationId: int
    applicationDate: date
    candidateId: int
    candidateFirstname: str
    candidateLastname: str
    candidateEmail: str
    jobId: int
    jobName: str
