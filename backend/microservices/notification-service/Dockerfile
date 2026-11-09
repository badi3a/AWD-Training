# =====================================================================
#  notification – image Docker (Python 3.12, FastAPI)
# =====================================================================
FROM python:3.12-slim

# Pas de fichiers .pyc, journaux affichés immédiatement (docker compose logs)
ENV PYTHONDONTWRITEBYTECODE=1 \
    PYTHONUNBUFFERED=1

WORKDIR /app

# 1. Dépendances d'abord : couche mise en cache tant que requirements.txt ne change pas
COPY requirements.txt .
RUN pip install --no-cache-dir -r requirements.txt

# 2. Code de l'application
COPY app ./app
COPY scripts ./scripts

EXPOSE 8084
# --host 0.0.0.0 : écouter sur toutes les interfaces du conteneur (pas seulement localhost)
CMD ["uvicorn", "app.main:app", "--host", "0.0.0.0", "--port", "8084"]
