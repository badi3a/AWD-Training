"""Microservice notification : FastAPI + consommateur RabbitMQ.

Lancement :  python -m uvicorn app.main:app --port 8084
         ou  python -m app.main
"""
import logging
from contextlib import asynccontextmanager

from fastapi import FastAPI
from py_eureka_client.eureka_client import EurekaClient
from app.config import settings
from app.messaging.consumer import ApplicationSubmittedConsumer
from app.routers import notification
from app.services.notification_service import notification_service
logging.basicConfig(level=logging.INFO, format="%(asctime)s %(levelname)-7s [%(name)s] %(message)s")
logging.getLogger("aio_pika").setLevel(logging.WARNING)
logging.getLogger("aiormq").setLevel(logging.WARNING)




@asynccontextmanager
async def lifespan(app: FastAPI):
    eureka = None
    if settings.eureka_enabled:
        options = dict(
            eureka_server=settings.eureka_server,
            app_name="NOTIFICATION",
            instance_port=settings.port,             # même port que celui passé à uvicorn
            instance_host=settings.eureka_instance_host,
        )
        if settings.eureka_instance_host == "localhost":
            options["instance_ip"] = "127.0.0.1"     # poste de développement (évite les cartes réseau virtuelles)
        eureka = EurekaClient(**options)
        await eureka.start()                   # register + heartbeat toutes les 30 s

    consumer = None
    if settings.rabbitmq_enabled:
        consumer = ApplicationSubmittedConsumer(settings.rabbitmq_url, notification_service)
        consumer.start()

    yield

    if consumer is not None:
        await consumer.stop()
    if eureka is not None:
        await eureka.stop()                    # désenregistrement propre
app = FastAPI(
    title="Notification microservice",
    description="Notifications du JobBoard. Réagit à l'événement ApplicationSubmitted publié dans RabbitMQ.",
    version="1.0.0",
    docs_url="/swagger-ui",
    openapi_url="/v3/api-docs",
    redoc_url="/redoc",
    lifespan=lifespan,
)
app.include_router(notification.router)


if __name__ == "__main__":
    import uvicorn

    uvicorn.run("app.main:app", host="0.0.0.0", port=settings.port)
