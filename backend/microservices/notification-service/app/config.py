"""Configuration lue dans les variables d'environnement (valeurs par défaut pour le poste de développement)."""
import os
from dataclasses import dataclass


@dataclass(frozen=True)
class Settings:
    port: int = int(os.getenv("PORT", "8084"))
    # amqp://utilisateur:mot_de_passe@hôte:port/  (mêmes identifiants que docker-compose.yml)
    rabbitmq_url: str = os.getenv("RABBITMQ_URL", "amqp://awd:awd@localhost:5672/")
    # Mettre RABBITMQ_ENABLED=false pour lancer le service sans broker (tests de l'API seule)
    rabbitmq_enabled: bool = os.getenv("RABBITMQ_ENABLED", "true").lower() == "true"
    eureka_server: str = os.getenv("EUREKA_SERVER", "http://localhost:8761/eureka")
    eureka_enabled: bool = os.getenv("EUREKA_ENABLED", "true").lower() == "true"
    # Nom sous lequel les autres services joignent notification :
    # "localhost" sur le poste, "notification" (nom du service Docker) dans docker compose
    eureka_instance_host: str = os.getenv("EUREKA_INSTANCE_HOST", "localhost")

settings = Settings()
