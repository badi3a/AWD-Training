"""Pydantic schemas: they validate data AND generate the Swagger documentation."""
from pydantic import BaseModel, Field


class HelloResponse(BaseModel):
    message: str = Field(..., examples=["hello I'm microservice notification"])


# TODO (students): add the Notification schemas, for example:
#
# from datetime import datetime
# from enum import Enum
#
# class NotificationType(str, Enum):
#     EMAIL = "EMAIL"
#     SMS = "SMS"
#
# class NotificationStatus(str, Enum):
#     PENDING = "PENDING"
#     SENT = "SENT"
#
# class NotificationCreate(BaseModel):          # request body (POST / PUT)
#     recipient: str = Field(..., min_length=1, examples=["badia@example.com"])
#     subject: str = Field(..., min_length=1, max_length=150, examples=["Interview scheduled"])
#     message: str = Field(..., min_length=1, examples=["Your interview is on 2026-10-05 at 10:00"])
#     type: NotificationType = NotificationType.EMAIL
#     candidateId: int | None = Field(None, examples=[1])
#
# class Notification(NotificationCreate):       # response
#     id: int
#     status: NotificationStatus
#     createdAt: datetime
