"""Run with: pytest"""
from fastapi.testclient import TestClient

from app.main import app

client = TestClient(app)


def test_hello_returns_greeting():
    res = client.get("/api/notifications/hello")
    assert res.status_code == 200
    assert res.json() == {"message": "hello I'm microservice notification"}


def test_openapi_spec_is_exposed():
    res = client.get("/v3/api-docs")
    assert res.status_code == 200
    spec = res.json()
    assert spec["info"]["title"] == "Notification Microservice API"
    assert "/api/notifications/hello" in spec["paths"]


def test_swagger_ui_is_served():
    res = client.get("/swagger-ui")
    assert res.status_code == 200
    assert "swagger" in res.text.lower()


def test_unknown_route_returns_404():
    assert client.get("/api/unknown").status_code == 404


# TODO (students): add tests for the Notification endpoints.
