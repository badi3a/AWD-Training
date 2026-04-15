
# 🚀 Architecture Microservices : Du Monolithe vers l'Écosystème Distribué
📅 **Année universitaire : 2025–2026** 
Ce dépôt contient l'ensemble des ressources pédagogiques et techniques pour le module de développement d'architectures microservices. L'objectif est de transformer une application monolithique de recrutement en un système distribué résilient et scalable.

---

## 🏗️ Étude de Cas : Plateforme de Recrutement (Job Board)

Le projet s'appuie sur un système réel de gestion d'offres d'emploi.

### 1. Du Monolithe (Conceptuel) ...
Initialement, toutes les entités (Candidats, Jobs, Candidatures, Meetings) partagent le même espace et la même base de données.
* **Schéma :** ![class-diagram](https://github.com/badi3a/AWD-Training/blob/main/documentation/diag/class-diagram.png)

### 2. ... Vers les Domaines Microservices (DDD)
L'application est découpée en domaines métier autonomes (Bounded Contexts) :
* **Candidate Domain** (Microservice 1)
* **Job Domain** (Microservice 2)
* **Application Domain** (Microservice 3)
* **Notification Domain** (Microservice 4)
* **Meeting Domain** (Microservice 5)

* **Schéma de découpage :** ![domain-class-diagram](https://github.com/badi3a/AWD-Training/blob/main/documentation/diag/domain-class-diagram.png)

---

## 🛠️ Architecture Technique Globale

L'architecture finale (Validation **AA8**) intègre l'ensemble des composants d'infrastructure nécessaires à un environnement de production :

* **API Gateway :** Point d'entrée unique (Spring Cloud Gateway).
* **Service Discovery :** Annuaire dynamique (Netflix Eureka).
* **Config Server :** Gestion centralisée des propriétés (Git/Native).
* **Message Broker :** Communication asynchrone (RabbitMQ).
* **Identity Provider :** Sécurisation des accès (Keycloak).

* **Schéma Global :** ![microservices-global-architecture](https://github.com/badi3a/AWD-Training/blob/main/documentation/diag/microservices-global-architecture.png)

---

## 📦 Stack Technologique

| Composant | Technologie |
| :--- | :--- |
| **Langages** | Java (Spring Boot), Python (FastAPI), Node.js |
| **Bases de données** | H2, MySQL, MongoDB |
| **Infrastructure** | Docker, Docker Compose |
| **Communication** | REST (Synchrone) & RabbitMQ (Asynchrone) |
| **Front End** | Angular |

---

## 🏫 Cadre pédagogique

### Enseignante : [Badia Bouhdid](https://www.linkedin.com/in/badiabouhdid)

Ce workshop a été développé dans le cadre du module **Applications Web Distribuées**,  
à l’**École d’Ingénieurs ESPRIT**.
