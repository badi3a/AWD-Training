"""Notification routes: /api/notifications/..."""
from fastapi import APIRouter

from app.schemas import HelloResponse

router = APIRouter(prefix="/api/notifications", tags=["Notifications"])


@router.get(
    "/hello",
    response_model=HelloResponse,
    summary="Hello from the notification microservice",
    description="Checks that the microservice is up and returns a greeting message.",
)
def hello() -> HelloResponse:
    return HelloResponse(message="hello I'm microservice notification")


# TODO (students): implement the Notification logic.
# No database: keep notifications in memory, for example:
#
# notifications: list[Notification] = []
# next_id = 1
#
# @router.get("", response_model=list[Notification], summary="List notifications")
# def find_all(): ...
#
# @router.get("/{notification_id}", response_model=Notification, summary="Get a notification")
# def find_by_id(notification_id: int): ...            # raise HTTPException(404, ...) if not found
#
# @router.post("", response_model=Notification, status_code=201, summary="Create a notification")
# def create(body: NotificationCreate): ...
#
# @router.put("/{notification_id}", response_model=Notification, summary="Update a notification")
# def update(notification_id: int, body: NotificationCreate): ...
#
# @router.patch("/{notification_id}/send", response_model=Notification, summary="Mark as sent")
# def send(notification_id: int): ...
#
# @router.delete("/{notification_id}", status_code=204, summary="Delete a notification")
# def delete(notification_id: int): ...
#
# Swagger is generated automatically from these decorators and the Pydantic schemas.
