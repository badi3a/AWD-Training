# Workshop 2 – Implémentation du serveur Eureka (Service Discovery)

🎓 **Formation : Microservices**  
📅 **Année universitaire : 2025–2026**  
🧑‍💻 **Workshop 2**

---

## 🎯 Objectif du workshop

L’objectif de ce workshop est de mettre en place un **serveur Eureka** afin de permettre la **découverte dynamique des microservices** dans une architecture distribuée.

À la fin de ce workshop, l’étudiant sera capable de :

- Comprendre le principe de **Service Discovery**
- Créer et configurer un **Eureka Server**
- Enregistrer des microservices comme **Eureka Clients**
- Visualiser les instances enregistrées via l’interface Eureka
- Comprendre le mécanisme d’enregistrement et de renouvellement des services

---

## 🧩 Architecture mise en place

Dans ce workshop, nous mettons en place :

- 🖥️ Un **Eureka Server**
- 📦 Un ou plusieurs **microservices clients**
- 🔁 Enregistrement automatique des services
- 📊 Visualisation des instances via le dashboard Eureka

---

## 🛠️ Technologies utilisées

- Java 17
- Spring Boot
- Spring Cloud Netflix Eureka
- Maven
- IntelliJ IDEA

---

## 📄 Énoncé du workshop

L’énoncé détaillé du Workshop 2 est disponible au format PDF :

👉 [Télécharger l’énoncé du Workshop 2](https://github.com/badi3a/AWD-Training/blob/main/Atelier_Eureka%20server.pdf)

---

## 📝 Travail à faire (Homework)

👉 Intégrer le serveur Eureka dans l’architecture existante contenant :

- Microservice **Candidat**
- Microservice **Job**
- Gateway

Chaque microservice doit :

- Être enregistré automatiquement dans Eureka
- Être visible dans le dashboard (http://localhost:8761)
- Pouvoir être exécuté sur plusieurs instances (ports différents)

---

## ✅ Rendu attendu

- Un projet **Eureka Server** fonctionnel
- Les microservices configurés comme **Eureka Clients**
- Enregistrement réussi des services dans le dashboard
- Plusieurs instances visibles pour au moins un microservice
- Code structuré et fonctionnel
- Projet poussé sur **GitHub**

---

💡 **Conseil :**  
Démarrez d’abord le serveur Eureka avant d’exécuter les microservices clients.

🚀 Bon courage et bonne implémentation !

---

## 🏫 Cadre pédagogique

### Enseignante : [Badia Bouhdid](https://www.linkedin.com/in/badiabouhdid)

Ce workshop a été développé dans le cadre du module **Applications Web Distribuées**,  
à l’**École d’Ingénieurs ESPRIT**.
