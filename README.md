# Workshop 3 – Implémentation de l’API Gateway

🎓 **Formation : Microservices**  
📅 **Année universitaire : 2026–2027**  
🧑‍💻 **Workshop 3**

---

## 🎯 Objectif du workshop

L’objectif de ce workshop est de mettre en place une **API Gateway** jouant le rôle de point d’entrée unique vers les microservices.

À la fin de ce workshop, l’étudiant sera capable de :

- Comprendre le rôle d’une API Gateway dans une architecture microservices
- Mettre en place une configuration **statique**
- Mettre en place une configuration **dynamique avec Eureka**
- Observer le mécanisme de **load balancing**
- Comparer les approches statique et dynamique

---

## 🧩 Architecture mise en place

Dans ce workshop, nous mettons en œuvre :

- 🖥️ Un **API Gateway**
- 📦 Les microservices existants (Candidat, Job)
- 🔁 Routage des requêtes via la Gateway
- ⚖️ Load balancing automatique via Eureka

L’API Gateway agit comme un **intermédiaire intelligent** qui :

- Reçoit les requêtes clients
- Sélectionne une instance disponible
- Redirige la requête vers le microservice approprié

---

## 🛠️ Technologies utilisées

- Java 17
- Spring Boot
- Spring Cloud Gateway
- Spring Cloud Netflix Eureka Client
- Maven
- IntelliJ IDEA

---

## 📄 Énoncé du workshop

L’énoncé détaillé du Workshop 3 est disponible au format PDF :

👉 [Télécharger l’énoncé du Workshop 3](Implémentation API Gateway.pdf)

---

## 📝 Travail à faire (Rendu)

- Implémenter une **configuration statique**
- Implémenter une **configuration dynamique avec Eureka**
- Activer les logs de routage
- Identifier l’algorithme de load balancing utilisé
- Tester l’algorithme **Random** uniquement pour le service Candidat
- Comparer les deux approches (statique vs dynamique)

---

## ⚖️ Comparaison des approches

| Approche | Utilise Eureka ? | Avantages | Inconvénients |
|-----------|------------------|------------|---------------|
| Dynamique (lb://service) | ✅ Oui | Pas besoin de connaître l’URL exacte, support multi-instances, load balancing automatique | Dépendance à Eureka |
| Statique (uri http://...) | ❌ Non | Simple et rapide à configurer | Non adapté aux environnements dynamiques |

---

## ✅ Rendu attendu

- Projet **API Gateway fonctionnel**
- Routage correct vers les microservices
- Intégration réussie avec Eureka
- Load balancing observable en console
- Code structuré et propre
- Projet poussé sur **GitHub**

---

💡 **Conseil :**  
Démarrez toujours le serveur Eureka avant de lancer l’API Gateway et les microservices.

🚀 Bon courage et bonne implémentation !

---

## 🏫 Cadre pédagogique

### Enseignante : [Badia Bouhdid](https://www.linkedin.com/in/badiabouhdid)

Ce workshop a été développé dans le cadre du module **Applications Web Distribuées**,  
à l’**École d’Ingénieurs ESPRIT**.
