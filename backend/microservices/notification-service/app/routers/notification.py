"""Routes /api/notifications.

Seules les routes de lecture sont fournies : elles servent à vérifier le scénario RabbitMQ.
Le CRUD complet (POST, PUT, PATCH /send, DELETE) reste le TODO de l'atelier précédent.
"""
from fastapi import APIRouter, HTTPException

from app.schemas import Notification
from app.services.notification_service import notification_service

router = APIRouter(prefix="/api/notifications", tags=["notifications"])


@router.get("/hello", summary="Vérifier que le service répond")
def hello() -> dict:
    return {"message": "hello I'm microservice notification"}


@router.get("", summary="Lister les notifications", response_model=list[Notification])
def find_all() -> list[Notification]:
    return notification_service.find_all()


@router.get("/{notification_id}", summary="Lire une notification", response_model=Notification)
def find_by_id(notification_id: int) -> Notification:
    notification = notification_service.find_by_id(notification_id)
    if notification is None:
        raise HTTPException(status_code=404, detail=f"Notification {notification_id} not found")
    return notification
