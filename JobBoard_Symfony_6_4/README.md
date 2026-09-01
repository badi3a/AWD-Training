# JobBoard — Symfony 6.4 Teaching Monolith

JobBoard is a deliberately **monolithic** recruiting platform built with Symfony 6.4. It supports the architectural-diagnosis activity in the *Distributed Web Applications* module. Students are expected to inspect the project, identify evidence of coupling and shared deployment constraints, and discuss an evolution path. The repository is not presented as an ideal production architecture and it is not a microservices implementation.

## Learning purpose

The project gives students a concrete codebase for analyzing:

- one deployable Symfony application;
- one shared relational database;
- shared Doctrine entities and object relationships;
- direct in-process collaboration between modules;
- centralized authentication and authorization;
- common repository, release and runtime constraints;
- transactions that are easy locally but harder to preserve after distribution.

## Functional scope

### Public users

- browse and search published jobs;
- view job details;
- log in.

### Candidates

- hold a candidate profile and skills;
- apply once to a published job;
- provide a cover letter;
- follow application status.

### Companies

- hold a company profile;
- create draft or published jobs;
- view application counts.

### Administrators

- access a global dashboard with candidate, company, job and application totals.

## Technical stack

- PHP 8.2 or later;
- Symfony 6.4 LTS components;
- Doctrine ORM with PHP attributes;
- Twig server-side rendering;
- Symfony Security form login and role-based access control;
- Symfony Validator and CSRF protection;
- Doctrine Fixtures for demonstration data;
- SQLite by default, with a documented MySQL alternative;
- plain CSS to keep the teaching repository easy to inspect.

Symfony 6.4 documentation covers Framework, Doctrine ORM, Security, Forms, Validation and related components. The fixture class follows the standard Doctrine fixture approach: create objects, persist them and flush the object manager.

## Technical constraints

The following constraints are intentional and should be considered during architectural analysis.

1. **Single deployment unit**  
   Every feature is compiled, released and deployed as one Symfony application.

2. **Shared database**  
   Candidate, Company, Job and Application data live in the same relational schema. Doctrine relations allow joins and local transactions across functional areas.

3. **Centralized identity model**  
   One `User` entity and one security firewall serve administrators, recruiters and candidates.

4. **Synchronous in-process business calls**  
   `ApplicationManager` loads and validates entities directly and commits one local transaction. There is no network communication or asynchronous messaging.

5. **Shared release lifecycle**  
   A small change in one feature requires validation and deployment of the whole application.

6. **Coarse-grained scalability**  
   The complete PHP application must be replicated even when only job search or candidate traffic increases.

7. **Shared failure domain**  
   Resource exhaustion, schema problems or a critical application defect can affect all functions.

8. **No public REST API**  
   This baseline uses controllers and Twig. API design is introduced later in the module.

9. **Local file-system placeholder**  
   `public/uploads/` is provided for teaching purposes. In a distributed or cloud deployment, shared object storage would normally be considered.

10. **No container or cloud configuration in the baseline**  
    Docker, Kubernetes, service discovery, API Gateway and centralized configuration are intentionally excluded from this first-state monolith.

## Project structure

```text
JobBoard_Symfony_6_4/
├── bin/console
├── config/
│   ├── packages/
│   ├── routes.yaml
│   └── services.yaml
├── public/
│   ├── css/app.css
│   ├── index.php
│   └── uploads/
├── src/
│   ├── Controller/
│   ├── DataFixtures/
│   ├── Entity/
│   ├── Repository/
│   └── Service/
├── templates/
├── tests/
├── .env
├── composer.json
└── README.md
```

## Installation

### 1. Prerequisites

Install:

- PHP 8.2+;
- Composer 2;
- PHP extensions required by Symfony and the selected database driver;
- SQLite, or MySQL 8 if the alternative connection is used.

### 2. Install dependencies

```bash
composer install
```

### 3. Configure the database

The default `.env` uses SQLite:

```dotenv
DATABASE_URL="sqlite:///%kernel.project_dir%/var/jobboard.db"
```

For MySQL, create `.env.local` and use a connection adapted to the local machine:

```dotenv
DATABASE_URL="mysql://jobboard:jobboard@127.0.0.1:3306/jobboard?serverVersion=8.0&charset=utf8mb4"
```

### 4. Create the schema

For the teaching baseline:

```bash
php bin/console doctrine:database:create --if-not-exists
php bin/console doctrine:schema:create
```

For a team workflow, generate and commit Doctrine migrations instead:

```bash
php bin/console make:migration
php bin/console doctrine:migrations:migrate
```

### 5. Load demonstration data

```bash
php bin/console doctrine:fixtures:load --no-interaction
```

The fixture command purges existing data by default.

### 6. Run the application

With Symfony CLI:

```bash
symfony server:start
```

Or with PHP's development server:

```bash
php -S 127.0.0.1:8000 -t public
```

Open `http://127.0.0.1:8000` in the browser.

## Demonstration accounts

All fixture accounts use the password `password`.

| Role | Email |
|---|---|
| Administrator | `admin@jobboard.local` |
| Company recruiter | `recruiter@jobboard.local` |
| Candidate | `candidate@jobboard.local` |

These credentials are strictly for local teaching use. Never use them in a real environment.

## Useful commands

```bash
php bin/console about
php bin/console debug:router
php bin/console doctrine:schema:validate
php bin/console doctrine:fixtures:load --no-interaction
php bin/console cache:clear
```

## Suggested student analysis path

1. Inspect `config/packages/security.yaml` and identify centralized roles.
2. Inspect Doctrine relationships in `src/Entity/`.
3. Analyze `ApplicationManager` and identify synchronous coupling.
4. Compare public, candidate, company and admin controllers.
5. Identify the shared database and deployment assumptions.
6. Document evidence using file paths and class names.
7. Separate facts, symptoms, causes, impacts and uncertainties.
8. Propose required architectural qualities before proposing technologies.

## Production hardening not included

Before real production use, the project would need, among other concerns:

- registration and account verification;
- password-reset workflow;
- authorization voters for entity ownership;
- robust form classes and validation coverage;
- safe CV/image upload scanning and object storage;
- paginated search and database indexing;
- email delivery and retry management;
- audit logging;
- rate limiting;
- security headers;
- automated tests and CI/CD;
- production secrets management;
- monitoring, logs and backup procedures.

## License

MIT for teaching and learning use.
