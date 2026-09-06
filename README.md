# 🌐 Applications Web Distribuées : du monolithe au Cloud Native

📅 **Année universitaire : 2026–2027**  
🏫 **École : ESPRIT, École d’Ingénieurs**  
🎓 **Public : 4e année, Cycle Ingénieur en Informatique, Génie Logiciel**  
🏷️ **Code du module : MT-41**  
🏢 **Unité pédagogique : UP WEB**

---

## 📘 Présentation du module

Ce dépôt regroupe les ressources pédagogiques et techniques du module **Applications Web Distribuées**.

Le module accompagne progressivement les étudiants dans l’étude, la conception, la réalisation, la documentation, la sécurisation et le déploiement d’applications Web distribuées.

Le parcours commence par l’analyse d’une application monolithique, puis introduit les API REST, la décomposition en Microservices, la découverte de services, l’API Gateway, les communications synchrones et asynchrones, la sécurité et la conteneurisation.

La progression pédagogique suit le fil conducteur suivant :

```text
Monolithe MVC
      ↓
API REST et documentation OpenAPI
      ↓
Décomposition en Microservices
      ↓
Service Discovery et observabilité
      ↓
API Gateway
      ↓
Communication synchrone et asynchrone
      ↓
Sécurité distribuée
      ↓
Conteneurisation et déploiement Cloud Native
```

---

## 🎯 Vision du module

Le module vise à former des ingénieurs capables de :

- comprendre l’évolution des architectures logicielles ;
- analyser les besoins métier et les contraintes techniques ;
- comparer plusieurs styles architecturaux ;
- concevoir des contrats d’API cohérents ;
- décomposer une application selon ses capacités métier ;
- mettre en œuvre les mécanismes nécessaires à une architecture distribuée ;
- documenter, tester, sécuriser et déployer une solution ;
- justifier les choix technologiques et architecturaux réalisés.

---

## 🧠 Acquis d’apprentissage

À l’issue du module, l’étudiant sera capable de :

- **AA1 : Expliquer** l’évolution des architectures logicielles jusqu’aux architectures distribuées modernes.
- **AA2 : Comparer et analyser** les architectures monolithique, SOA et Microservices selon les besoins métier et les contraintes techniques.
- **AA3 : Analyser** les besoins métier conduisant à l’adoption d’une architecture distribuée.
- **AA4 : Expliquer** les principes fondamentaux d’une architecture Microservices.
- **AA5 : Évaluer** les avantages, les limites et les défis des architectures Microservices.
- **AA6 : Sélectionner** les outils et technologies adaptés à la conception et au déploiement d’une architecture distribuée.
- **AA7 : Appliquer** les principes de l’architecture Microservices dans un environnement distribué.
- **AA8 : Concevoir, implémenter, déployer et valider** une application distribuée orientée Cloud Native.

---

## 🧭 Philosophie pédagogique

Le module adopte une approche active, progressive et centrée sur la mise en situation de l’étudiant.

### Project-Based Learning

Le projet **JobBoard** constitue le fil rouge du module. Chaque chapitre introduit une nouvelle problématique architecturale et fait évoluer le même système.

### Approche CDIO

Les étudiants suivent un cycle complet d’ingénierie :

```text
Concevoir → Développer → Implémenter → Opérer
```

### Learning by Doing

Les notions théoriques sont mobilisées dans des ateliers pratiques, des Prosits, des activités d’analyse et des livrables techniques.

### Active Learning

Les étudiants participent à :

- des études de cas ;
- des diagnostics architecturaux ;
- des activités individuelles et collectives ;
- des discussions argumentées ;
- des recherches guidées ;
- des démonstrations ;
- des revues et validations intermédiaires.

### Usage responsable de l’intelligence artificielle

L’utilisation d’outils d’intelligence artificielle peut être autorisée selon les consignes de chaque activité.

Les étudiants restent responsables :

- du code remis ;
- des choix techniques ;
- des tests réalisés ;
- des corrections apportées ;
- de la compréhension des éléments générés ;
- de la traçabilité des prompts lorsque celle-ci est demandée.

L’IA doit soutenir l’analyse et la production, sans remplacer la réflexion de l’étudiant.

---

## 🔄 Cycle d’une séquence d’apprentissage

Chaque séquence peut combiner les étapes suivantes :

1. **Cours** : concepts, fondements et patterns ;
2. **Atelier** : mise en œuvre sur le projet fil rouge ;
3. **Prosit** : analyse d’une situation-problème ;
4. **Réflexion** : recul critique, comparaison et justification ;
5. **Livrable** : code, documentation, diagrammes, dépôt Git ou démonstration.

---

## 🧩 Projet fil rouge : JobBoard

**JobBoard** est une plateforme pédagogique de recrutement utilisée pour illustrer la transformation progressive d’une application Web.

Le domaine couvre notamment :

- les candidats ;
- les entreprises et les recruteurs ;
- les offres d’emploi ;
- les candidatures ;
- les entretiens ;
- les notifications.

### Évolution pédagogique du projet

1. analyse d’un monolithe MVC existant ;
2. diagnostic architectural ;
3. exposition d’API REST ;
4. documentation OpenAPI et Swagger ;
5. décomposition en services métier ;
6. ajout du Service Discovery et de l’observabilité ;
7. mise en place de l’API Gateway ;
8. communication interservices ;
9. sécurisation ;
10. conteneurisation et déploiement reproductible.

### Services métier cibles

```text
Candidate Service
Company Service
Job Service
Application Service
Meeting Service
Notification Service
```

---

## 📚 Programme du module

### Chapitre 1 : Introduction aux architectures distribuées et SOA

- évolution des architectures logicielles ;
- architecture monolithique ;
- architecture N-tiers ;
- SOA et services Web ;
- principes des systèmes distribués ;
- diagnostic architectural de JobBoard.

### Chapitre 2 : API REST et documentation

- principes REST ;
- ressources et URI ;
- méthodes HTTP ;
- codes de statut ;
- JSON ;
- OpenAPI et Swagger ;
- conception, documentation et test d’API.

### Chapitre 3 : Migration vers les Microservices

- principes Microservices ;
- décomposition métier ;
- bounded contexts ;
- autonomie des services ;
- déploiement indépendant ;
- communication interservices.

### Chapitre 4 : Service Discovery et observabilité

- découverte de services ;
- enregistrement et heartbeat ;
- registre de services ;
- health, info et metrics ;
- observabilité d’une architecture distribuée.

### Validation intermédiaire

- validation de la conception ;
- architecture proposée ;
- diagrammes ;
- organisation Git ;
- documentation ;
- démonstration du projet.

### Chapitre 5 : API Gateway

- point d’entrée unique ;
- routage ;
- load balancing ;
- centralisation des préoccupations transverses ;
- documentation des accès.

### Chapitre 6 : Communication synchrone

- appels interservices ;
- contrats ;
- gestion des erreurs ;
- délais d’attente ;
- résilience.

### Chapitre 7 : Communication asynchrone

- événements métier ;
- broker de messages ;
- découplage temporel ;
- cas d’usage asynchrones ;
- cohérence éventuelle.

### Chapitre 8 : Sécurité distribuée

- identité ;
- authentification ;
- autorisation ;
- rôles ;
- tokens ;
- protection des endpoints.

### Chapitre 9 : Conteneurisation

- images et conteneurs ;
- configuration des services ;
- composition multi-conteneurs ;
- déploiement reproductible ;
- transition vers le Cloud Native.

---

## 🛠️ Choix technologique

Le module n’impose pas un stack backend unique pour tous les ateliers et projets.

Selon les consignes de l’activité, les étudiants peuvent choisir une technologie appropriée, par exemple :

- Symfony et PHP ;
- Node.js avec Express ou NestJS ;
- Django REST Framework ou FastAPI ;
- Spring Boot ;
- .NET Web API ;
- une technologie équivalente, pertinente et justifiée.

Quel que soit le stack retenu, les productions doivent respecter les exigences communes du module :

- contrat REST cohérent ;
- URI orientées ressources ;
- méthodes HTTP adaptées ;
- statuts HTTP explicites ;
- validation des entrées ;
- réponses JSON structurées ;
- gestion des erreurs ;
- documentation OpenAPI et Swagger ;
- scénarios de test reproductibles ;
- instructions d’installation et d’exécution ;
- justification des choix techniques.

---

## 🧰 Outils recommandés

### Développement et exécution

- un environnement de développement adapté au stack choisi ;
- le runtime et le gestionnaire de dépendances associés ;
- Git ;
- un compte GitHub ou GitLab.

### Documentation et test d’API

- Swagger UI et OpenAPI ;
- Postman ;
- Insomnia ;
- curl ;
- tests automatisés selon le framework choisi.

### Architecture et modélisation

- diagrams.net ;
- Mermaid ;
- PlantUML ;
- un outil équivalent de modélisation.

### Infrastructure

- Docker ;
- Docker Compose ;
- une base de données adaptée au projet ;
- des outils de développement local propres au stack sélectionné.


---

## 📊 Modalités de validation

La note finale du module est répartie comme suit :

| Composante | Pondération | Éléments évalués |
| :--- | :---: | :--- |
| **Projet** | **40 %** | Architecture, réalisation, documentation, tests, Git, démonstration et justification des choix |
| **Contrôle continu** | **20 %** | Cours notés, Prosits, ateliers, quiz et livrables intermédiaires |
| **Examen théorique** | **40 %** | Compréhension des concepts, analyse architecturale et justification technique |

```text
Note finale = 0,40 × Projet + 0,20 × Contrôle continu + 0,40 × Examen théorique
```

---

## ✅ Critères généraux de qualité

Les productions sont évaluées selon les critères pertinents pour l’activité :

- compréhension du problème ;
- cohérence de l’architecture ;
- qualité du contrat d’API ;
- exactitude technique ;
- fonctionnement de la solution ;
- gestion des erreurs ;
- qualité des tests ;
- qualité de la documentation ;
- reproductibilité ;
- lisibilité du code et des diagrammes ;
- justification des choix ;
- recul critique ;
- contribution au travail d’équipe ;
- usage responsable de l’intelligence artificielle.

---

## 🏫 Cadre pédagogique

### Enseignante responsable

**[Dr Badia Bouhdid](https://www.linkedin.com/in/badiabouhdid)**  
PhD en Informatique, IT Assistant Professor, UP WEB  
📧 [badiaa.bouhdid@esprit.tn](mailto:badiaa.bouhdid@esprit.tn)

Le module **Applications Web Distribuées** est dispensé à l’[École d’Ingénieurs ESPRIT](https://www.esprit.tn).

---

## 🔗 Liens utiles

- [Site officiel ESPRIT](https://www.esprit.tn)
- [Profil LinkedIn de l’enseignante](https://www.linkedin.com/in/badiabouhdid)
- [Chaîne YouTube](https://www.youtube.com/channel/UCvxNQew_Sj6KQWR4jZazuIw)
- [Publications Medium](https://medium.com/@badiaabouhdid)
- [Profil ResearchGate](https://www.researchgate.net/profile/Badia-Bouhdid)

---

## 📝 Message aux étudiants

> Ne commencez pas par choisir une technologie. Commencez par comprendre le besoin, identifier les contraintes, définir les qualités attendues et concevoir un contrat clair.

Une architecture distribuée ne constitue pas une solution universelle. Elle apporte de l’autonomie, de la flexibilité et des possibilités de déploiement indépendant, mais introduit aussi de nouvelles responsabilités liées au réseau, aux pannes partielles, à la cohérence des données, à la sécurité, à l’observabilité et à l’exploitation.

**Bon apprentissage et bon développement du projet JobBoard !**
