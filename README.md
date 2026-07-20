# 🚀 JobBoard REST API

> **Support pédagogique — Chapitre 2 · Module Applications Web Distribuées (ESPRIT)**
> API REST professionnelle pour la plateforme JobBoard, construite avec **Node.js + Express + MySQL** conformément au diagramme UML fourni.
> Documentation interactive **Swagger UI** incluse.

---

## 📚 Table des matières

1. [Présentation](#-présentation)
2. [Objectifs pédagogiques](#-objectifs-pédagogiques)
3. [Architecture générale](#-architecture-générale)
4. [Technologies](#-technologies)
5. [Installation](#-installation)
6. [Configuration](#-configuration)
7. [Création de la base](#-création-de-la-base)
8. [Lancement](#-lancement)
9. [Documentation Swagger](#-documentation-swagger)
10. [API déjà développées](#-api-déjà-développées)
11. [Travail demandé aux étudiants](#-travail-demandé-aux-étudiants)
12. [Arborescence du projet](#-arborescence-du-projet)
13. [Comment tester](#-comment-tester)
14. [Exemples d'utilisation](#-exemples-dutilisation)
15. [Dépannage](#-dépannage)

---

## 🎯 Présentation

Ce projet est un **backend REST monolithique** qui expose l'API de la plateforme d'emploi **JobBoard**. Il est construit à partir du **diagramme de classes UML** fourni avec l'énoncé de l'atelier.

Il est destiné à servir de **support technique pour l'Atelier 1** du Chapitre 2. Deux modules seulement — **Candidate** et **Address** — sont entièrement implémentés par l'enseignant. Les autres modules (**Application**, **Job**, **Notification**, **Meeting**) sont fournis sous forme de squelettes avec des commentaires pédagogiques `TODO` que les étudiants devront compléter durant le TP.

### Modèle du domaine

Le diagramme UML fourni définit 7 entités et leurs relations :

| Entité         | Rôle                                                       | Statut    |
| -------------- | ---------------------------------------------------------- | --------- |
| `Candidate`    | Candidats à un emploi                                       | ✅ Fourni |
| `Address`      | Adresse postale d'un candidat                               | ✅ Fourni |
| `Application`  | Candidature d'un candidat à une offre                       | 🔧 À faire par les étudiants |
| `Job`          | Offre d'emploi                                              | 🔧 À faire |
| `Category`     | Catégorie d'une offre                                       | 🔧 À faire |
| `Meeting`      | Entretien lié à une candidature                             | 🔧 À faire |
| `Notification` | Notification envoyée suite à une candidature                | 🔧 À faire |

**Relations principales** (extraites du diagramme UML) :

- `Candidate` **1—1** `Address`
- `Candidate` **1—N** `Application`
- `Application` **N—1** `Job`
- `Job` **N—1** `Category`
- `Application` **1—1** `Meeting`
- `Application` **1—1** `Notification`

---

## 🎓 Objectifs pédagogiques

À travers ce projet, les étudiants apprennent à :

- ✅ Structurer une API en **couches MVC** (routes → controllers → services → models).
- ✅ Écrire des **requêtes SQL paramétrées** pour prévenir les injections.
- ✅ **Valider les entrées** utilisateur avec `express-validator`.
- ✅ Gérer proprement les **codes HTTP** (200, 201, 204, 400, 404, 409, 500).
- ✅ **Documenter une API** avec OpenAPI 3 et Swagger UI.
- ✅ Configurer les **middlewares essentiels** (Helmet, CORS, Morgan).
- ✅ Séparer **configuration** (.env) et **code source**.
- ✅ Comprendre le passage **du modèle UML** au **schéma SQL** puis aux **endpoints REST**.

---

## 🧱 Architecture générale

L'application suit une **architecture web Distribuée avec un backend RESTFul monolithique** inspirée du pattern Symfony que les étudiants connaissent déjà :

![JobBoard RESTful Architecture](https://github.com/badi3a/AWD-Training/blob/W01-Developper_MicroService/documentation/l'architecture%20globale.png))

*Figure 1. Monolithic RESTful architecture of the JobBoard application.*

```

```

Chaque couche a une **responsabilité unique** (Single Responsibility Principle du SOLID). Ce cloisonnement facilite la maintenance et permet aux étudiants de comprendre où intervenir pour chaque type de modification.

---

## ⚙️ Technologies

| Technologie          | Version    | Rôle                                                |
| -------------------- | ---------- | --------------------------------------------------- |
| **Node.js**          | ≥ 18       | Environnement d'exécution JavaScript côté serveur   |
| **Express.js**       | 4.19       | Framework Web léger et flexible                     |
| **MySQL**            | ≥ 8.0      | Base de données relationnelle                       |
| **mysql2**           | 3.10       | Driver MySQL avec API Promise                       |
| **dotenv**           | 16.4       | Chargement des variables d'environnement            |
| **swagger-ui-express**| 5.0       | Interface Swagger UI intégrée                       |
| **swagger-jsdoc**    | 6.2        | Génération OpenAPI depuis les commentaires JSDoc    |
| **express-validator**| 7.1        | Validation des entrées HTTP                         |
| **helmet**           | 7.1        | Sécurisation des en-têtes HTTP                      |
| **cors**             | 2.8        | Autorisation Cross-Origin                           |
| **morgan**           | 1.10       | Journalisation des requêtes HTTP                    |
| **nodemon**          | 3.1        | Redémarrage automatique en développement            |

**Volontairement exclus** : TypeScript, NestJS, Sequelize, Prisma — pour rester au plus près des concepts REST/SQL enseignés au Chapitre 2.

---

## 🛠️ Installation

### 1. Cloner le dépôt

```bash
git clone <URL-fournie-par-l-enseignant> jobboard-rest-api
cd jobboard-rest-api
```

### 2. Installer les dépendances

```bash
npm install
```

Cette commande télécharge toutes les dépendances listées dans `package.json` dans le dossier `node_modules/`.

---

## 🔧 Configuration

### 1. Créer votre fichier `.env`

Copiez le fichier d'exemple :

```bash
# macOS / Linux
cp .env.example .env

# Windows PowerShell
copy .env.example .env
```

### 2. Renseigner les valeurs

Ouvrez `.env` et adaptez à votre configuration MySQL locale :

```env
DB_HOST=localhost
DB_PORT=3306
DB_NAME=jobboard
DB_USER=root
DB_PASSWORD=       # Laissez vide si aucun mot de passe
PORT=3000
```

> ⚠️ **Sécurité :** le fichier `.env` est déjà ajouté au `.gitignore` — il ne doit **jamais** être commis dans un dépôt Git public.

---

## 🗄️ Création de la base

Le fichier `sql/create_database.sql` est **auto-suffisant** : il crée la base, les tables, les clés étrangères, les index et insère les données de démonstration.

### Option A — Ligne de commande MySQL

```bash
mysql -u root -p < sql/create_database.sql
```

### Option B — Depuis Node.js (script fourni)

```bash
npm run db:init
```

### Option C — phpMyAdmin

1. Ouvrez [http://localhost/phpmyadmin](http://localhost/phpmyadmin).
2. Cliquez sur l'onglet **Importer**.
3. Sélectionnez le fichier `sql/create_database.sql`.
4. Cliquez sur **Exécuter**.

### Vérification

Après exécution, la base `jobboard` doit exister avec 7 tables peuplées :

```sql
USE jobboard;
SHOW TABLES;
-- addresses, candidates, categories, jobs, applications, meetings, notifications

SELECT COUNT(*) FROM candidates;      -- doit renvoyer 5
SELECT COUNT(*) FROM addresses;       -- doit renvoyer 5
SELECT COUNT(*) FROM jobs;            -- doit renvoyer 5
SELECT COUNT(*) FROM applications;    -- doit renvoyer 4
```

---

## ▶️ Lancement

### En mode développement (rechargement automatique)

```bash
npm run dev
```

### En mode production

```bash
npm start
```

Une fois démarré, une bannière s'affiche :

```
──────────────────────────────────────────────────────────────────────────
  🚀  JobBoard REST API — ESPRIT · Module AWD · Chapitre 2
──────────────────────────────────────────────────────────────────────────
  ▸ Environnement    : development
  ▸ URL de base      : http://localhost:3000
  ▸ Documentation    : http://localhost:3000/api-docs
  ▸ Santé            : http://localhost:3000/health
──────────────────────────────────────────────────────────────────────────
```

---

## 📖 Documentation Swagger

Une fois le serveur démarré, ouvrez :

**👉 [http://localhost:3000/api-docs](http://localhost:3000/api-docs)**

L'interface Swagger UI affiche :

- Tous les endpoints implémentés (Candidates + Addresses).
- Les paramètres attendus et leurs types.
- Les corps de requête (Request Body) avec exemples JSON.
- Les codes de retour possibles et leurs formats de réponse.
- Un bouton **« Try it out »** pour tester chaque endpoint sans quitter le navigateur.

La spec OpenAPI brute est également disponible à :

```
http://localhost:3000/api-docs.json
```

Elle peut être importée directement dans Postman.

---

## ✅ API déjà développées

### 🧑 Ressource **Candidate**

| Méthode  | Endpoint                    | Description                              | Codes HTTP           |
| -------- | --------------------------- | ---------------------------------------- | -------------------- |
| `GET`    | `/api/candidates`           | Liste paginée (avec adresse imbriquée)   | 200                  |
| `GET`    | `/api/candidates/{id}`      | Détail d'un candidat                     | 200, 400, 404        |
| `POST`   | `/api/candidates`           | Création d'un candidat                    | 201, 400, 409        |
| `PUT`    | `/api/candidates/{id}`      | Modification d'un candidat                | 200, 400, 404, 409   |
| `DELETE` | `/api/candidates/{id}`      | Suppression d'un candidat                 | 204, 400, 404        |

**Validation** : firstname (2-80 caractères), lastname (2-80 caractères), email (format valide, unique).

### 📍 Ressource **Address**

| Méthode  | Endpoint                    | Description               | Codes HTTP     |
| -------- | --------------------------- | ------------------------- | -------------- |
| `GET`    | `/api/addresses`            | Liste paginée              | 200            |
| `GET`    | `/api/addresses/{id}`       | Détail d'une adresse       | 200, 400, 404  |
| `POST`   | `/api/addresses`            | Création                   | 201, 400       |
| `PUT`    | `/api/addresses/{id}`       | Modification               | 200, 400, 404  |
| `DELETE` | `/api/addresses/{id}`       | Suppression                | 204, 400, 404  |

**Validation** : street (2-150), houseNumber (1-20), zipCode (2-20).

---

## 🎓 Travail demandé aux étudiants

Les étudiants doivent implémenter les 4 modules restants en s'inspirant du patron des modules `candidates` et `addresses`. Un fichier `Not Implemented (501)` est renvoyé tant qu'un module n'est pas complété.

### 📋 Module **Application** — À réaliser

Représente la candidature d'un `Candidate` à un `Job`.

**Attributs** : `id`, `applicationDate`, `motivation`, `candidate_id`, `job_id`

**Travail :**
- ✅ CRUD REST complet (GET, POST, PUT, DELETE).
- ✅ Validation (voir express-validator).
- ✅ Documentation Swagger.
- ✅ Tests Postman.
- ✅ Gestion des cas d'erreur : 400 (candidate_id ou job_id inexistant), 409 (candidat a déjà postulé — contrainte d'unicité composite).

**Fichiers à compléter :**
- `models/applicationModel.js`
- `controllers/applicationController.js`
- `routes/applicationRoutes.js`
- **À créer** : `services/applicationService.js`, `middlewares/applicationValidator.js`

### 💼 Module **Job** — À réaliser

Représente une offre d'emploi.

**Attributs** : `id`, `name`, `description`, `available`, `date`, `category_id`

**Travail :**
- ✅ CRUD REST complet.
- ✅ Validation.
- ✅ Documentation Swagger.
- ✅ **Recherche** par nom (`?search=…`).
- ✅ **Pagination** (`?page=…&limit=…`).
- ✅ **Filtre** par catégorie (`?category_id=…`) et disponibilité (`?available=true/false`).

### 🔔 Module **Notification** — À réaliser

Notification envoyée automatiquement suite à une candidature.

**Attributs** : `id`, `sender`, `recipient`, `content`, `date`, `application_id`

**Travail :**
- ✅ CRUD REST complet.
- ✅ Gestion des notifications par destinataire (`?recipient=email@…`).
- ✅ Validation : email pour sender et recipient, application_id doit exister.

### 📅 Module **Meeting** — À réaliser

Entretien programmé suite à une candidature.

**Attributs** : `id`, `reference`, `link`, `status`, `application_id`

**Travail :**
- ✅ CRUD REST complet.
- ✅ Gestion des réunions.
- ✅ `reference` doit être unique (contrainte 409 Conflict).
- ✅ Machine à états sur `status` : `scheduled → held` ou `scheduled → cancelled`.
- ✅ **BONUS** : PATCH `/api/meetings/{id}/status` pour changer uniquement le statut.

---

## 📁 Arborescence du projet

```
jobboard-rest-api/
│
├── config/
│   └── database.js               # Pool MySQL + test de connexion
│
├── controllers/                   # Couche HTTP (traite req/res)
│   ├── candidateController.js    # ✅ Fourni
│   ├── addressController.js      # ✅ Fourni
│   ├── applicationController.js  # 🔧 À implémenter par les étudiants
│   ├── jobController.js          # 🔧 À implémenter
│   ├── notificationController.js # 🔧 À implémenter
│   └── meetingController.js      # 🔧 À implémenter
│
├── middlewares/                   # Middlewares Express
│   ├── candidateValidator.js     # Règles express-validator
│   ├── addressValidator.js
│   ├── errorHandler.js           # Handler centralisé des erreurs
│   ├── requestLogger.js          # Logger coloré en dev
│   └── validate.js               # Wrapper autour de validationResult
│
├── models/                        # Couche d'accès aux données (SQL manuel)
│   ├── candidateModel.js         # ✅ Fourni
│   ├── addressModel.js           # ✅ Fourni
│   ├── applicationModel.js       # 🔧 À implémenter
│   ├── jobModel.js               # 🔧 À implémenter
│   ├── notificationModel.js      # 🔧 À implémenter
│   └── meetingModel.js           # 🔧 À implémenter
│
├── routes/                        # Définition des URI et méthodes HTTP
│   ├── index.js                  # Monte /api/*
│   ├── system.js                 # /health, /
│   ├── candidateRoutes.js        # ✅ Fourni + doc Swagger
│   ├── addressRoutes.js          # ✅ Fourni + doc Swagger
│   ├── applicationRoutes.js      # 🔧 À implémenter
│   ├── jobRoutes.js              # 🔧 À implémenter
│   ├── notificationRoutes.js     # 🔧 À implémenter
│   └── meetingRoutes.js          # 🔧 À implémenter
│
├── services/                      # Logique métier
│   ├── candidateService.js       # ✅ Fourni
│   └── addressService.js         # ✅ Fourni
│
├── swagger/
│   └── swagger.js                # Configuration OpenAPI 3
│
├── sql/
│   └── create_database.sql       # Script SQL auto-exécutable
│
├── scripts/
│   └── init-db.js                # Alternative Node.js à mysql < script
│
├── docs/                         # Documentation additionnelle
│
├── app.js                        # Configuration Express
├── server.js                     # Démarrage HTTP
├── package.json
├── .env.example                  # Modèle de configuration
├── .gitignore
└── README.md                     # Ce fichier
```

---

## 🧪 Comment tester

Trois outils au choix, du plus simple au plus scriptable :

### 1. Swagger UI (recommandé pour débuter)

Ouvrez [http://localhost:3000/api-docs](http://localhost:3000/api-docs).

Chaque endpoint est déroulable avec :
- Documentation complète.
- Bouton **« Try it out »** qui envoie une requête réelle.

### 2. Postman

1. Ouvrez Postman.
2. Créez une nouvelle requête (bouton **+**).
3. Choisissez la méthode, l'URL (ex : `GET http://localhost:3000/api/candidates`).
4. Cliquez sur **Send**.

**Astuce :** vous pouvez importer directement la spec OpenAPI depuis l'URL `http://localhost:3000/api-docs.json` (menu **Import** → **Link**).

### 3. curl (ligne de commande)

Pratique pour scripter ou tester rapidement.

---

## 💡 Exemples d'utilisation

### Lister les candidats

```bash
curl http://localhost:3000/api/candidates
```

Réponse :
```json
{
  "data": [
    {
      "id": 1,
      "firstname": "Youssef",
      "lastname": "Mzoughi",
      "email": "youssef.mzoughi@example.tn",
      "address_id": 1,
      "address": {
        "id": 1,
        "street": "Avenue Habib Bourguiba",
        "houseNumber": "25",
        "zipCode": "1000"
      },
      "created_at": "2026-02-01T10:00:00.000Z",
      "updated_at": "2026-02-01T10:00:00.000Z"
    }
  ],
  "pagination": { "total": 5, "page": 1, "limit": 20, "totalPages": 1 }
}
```

### Créer une adresse

```bash
curl -X POST http://localhost:3000/api/addresses \
  -H "Content-Type: application/json" \
  -d '{
        "street": "Avenue Bourguiba",
        "houseNumber": "42",
        "zipCode": "1000"
      }'
```

### Créer un candidat lié à une adresse

```bash
curl -X POST http://localhost:3000/api/candidates \
  -H "Content-Type: application/json" \
  -d '{
        "firstname": "Nouveau",
        "lastname": "Candidat",
        "email": "nouveau@example.tn",
        "address_id": 3
      }'
```

### Mettre à jour un candidat

```bash
curl -X PUT http://localhost:3000/api/candidates/1 \
  -H "Content-Type: application/json" \
  -d '{
        "firstname": "Youssef",
        "lastname": "Mzoughi",
        "email": "youssef.new@example.tn"
      }'
```

### Supprimer un candidat

```bash
curl -X DELETE http://localhost:3000/api/candidates/1
```

Réponse : `204 No Content` (pas de corps).

### Cas d'erreur : email invalide

```bash
curl -X POST http://localhost:3000/api/candidates \
  -H "Content-Type: application/json" \
  -d '{"firstname":"Test","lastname":"Test","email":"invalid"}'
```

Réponse :
```json
{
  "status": 400,
  "message": "Erreur de validation",
  "errors": [{ "field": "email", "message": "L'email n'est pas au bon format." }]
}
```

---

## 🐛 Dépannage

### ❌ « Impossible de se connecter à la base de données »

- Vérifiez que **MySQL est démarré** (XAMPP/MAMP/WAMP ou service MySQL).
- Vérifiez le contenu de votre `.env` (mot de passe, port…).
- Assurez-vous que la base `jobboard` existe : `SHOW DATABASES;` dans MySQL.

### ❌ « Cannot find module 'express' »

Vous avez oublié d'installer les dépendances :
```bash
npm install
```

### ❌ « EADDRINUSE: address already in use :::3000 »

Le port 3000 est déjà utilisé. Deux solutions :
1. Changer le port dans `.env` : `PORT=3001`.
2. Tuer le processus qui l'occupe :
   ```bash
   # macOS / Linux
   lsof -ti:3000 | xargs kill -9
   # Windows PowerShell
   Get-Process -Id (Get-NetTCPConnection -LocalPort 3000).OwningProcess | Stop-Process
   ```

### ❌ Swagger UI ne se charge pas

- Vérifiez que le serveur tourne (`http://localhost:3000` doit répondre).
- Videz le cache du navigateur (Ctrl+F5).
- Ouvrez la console (F12) pour voir les erreurs.

### ❌ Erreur `501 Not Implemented` sur `/api/jobs`

C'est **normal** — ce module est à implémenter par les étudiants dans le TP.

---

## 📖 Références académiques

- **Fielding, R.** (2000). *Architectural Styles and the Design of Network-based Software Architectures*. PhD Thesis, UC Irvine.
- **Richardson, L. & Amundsen, M.** (2013). *RESTful Web APIs*. O'Reilly.
- **Masse, M.** (2011). *REST API Design Rulebook*. O'Reilly.
- **RFC 9110** — HTTP Semantics (IETF, 2022).
- **OpenAPI Specification 3.1** — [spec.openapis.org](https://spec.openapis.org/oas/latest.html).

---

## 📝 Licence

MIT — Utilisation libre pour l'enseignement, la recherche et les projets personnels.

---

<div align="center">

**esprit** · Module Applications Web Distribuées · AU 2026-2027

*Bon apprentissage à toutes et à tous !*

</div>
