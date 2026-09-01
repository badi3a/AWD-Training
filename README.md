# Prosit 01 — Architectural Diagnosis of JobBoard

## Distributed Web Applications

This repository contains the **JobBoard Symfony 6.4 monolithic application** used in Prosit 01 of the Distributed Web Applications module.

The objective of this activity is **not to immediately transform the application into microservices**. The objective is to examine an existing application, collect evidence from its source code, identify architectural constraints, and produce a clear and justified diagnosis.

> **Important:** Work individually before the classroom session. The individual diagnosis will be used as the starting point for team discussion and consolidation in class.

---

## 1. Learning objectives

By completing this Prosit, you should be able to:

- describe the main characteristics of a monolithic architecture;
- map the functional and technical structure of an existing Symfony MVC application;
- distinguish facts, symptoms, causes, impacts, and assumptions;
- identify architectural coupling in code, data, deployment, and organization;
- explain the limitations of a monolith in a growth context;
- translate business ambitions into functional requirements, quality attributes, and constraints;
- prioritize architectural problems using evidence;
- use an AI assistant critically without replacing your own analysis;
- produce a concise and evidence-based architectural diagnosis.

---

## 2. Case context

JobBoard is an employment platform that connects candidates and recruiters. The current solution is implemented as a **Symfony 6.4 MVC monolith**.

The application includes several functional areas:

- user and authentication management;
- candidate profiles and skills;
- company profiles;
- job publication and search;
- job applications;
- administration;
- shared security, configuration, persistence, and deployment mechanisms.

The project uses:

- PHP 8.2 or later;
- Symfony 6.4;
- Doctrine ORM;
- Twig;
- Symfony Security;
- a shared relational database;
- one source-code repository;
- one application runtime;
- one deployment unit.

These characteristics form the baseline that you must analyze.

---

## 3. Your mission

You are acting as a **junior software architecture consultant**.

Your mission is to answer the following question:

> What architectural characteristics of the current JobBoard application may limit its ability to evolve, scale, remain available, and support several teams?

Your work must be based on concrete evidence from the repository. Avoid generic statements such as:

- “Monoliths are always bad.”
- “Microservices are always scalable.”
- “The application must be migrated to the cloud.”

A strong diagnosis explains:

1. what is currently observed;
2. why it may become a problem;
3. who or what is affected;
4. what evidence supports the conclusion;
5. what additional information is still required.

---

## 4. Rules for the individual asynchronous work

Before the classroom session:

- work individually;
- inspect the source code without modifying it;
- take notes while exploring the project;
- record file paths, class names, relationships, and configuration elements as evidence;
- distinguish observations from interpretations;
- complete the individual diagnosis provided in Blackboard;
- export or save your diagnosis before coming to class.

Do not start designing the target microservices architecture during this phase. First diagnose the current state.

---

## 5. Recommended analysis path

### Step 1 — Understand the project structure

Start by examining the main directories:

```text
src/Controller/
src/Entity/
src/Repository/
src/Service/
src/DataFixtures/
config/packages/
templates/
public/
```

Questions to consider:

- Are functional areas physically separated?
- Which technical elements are shared?
- Which directories contain business logic?
- Does the project reveal clear module boundaries?

### Step 2 — Map the functional areas

Identify the main features handled by the application.

Look at:

- controllers;
- entities;
- services;
- templates;
- routes;
- roles and access-control rules.

Create a preliminary functional map. For example, determine which files relate to candidates, companies, jobs, applications, and administration.

### Step 3 — Examine the domain model

Inspect the Doctrine entities in `src/Entity/`.

Focus on:

- relationships between entities;
- shared identifiers;
- bidirectional associations;
- cascade operations;
- ownership of data;
- constraints that span several functional areas.

Questions to consider:

- Which entities depend directly on other domains?
- Can one functional area evolve without changing another?
- Which data would be difficult to separate?
- Which transactions currently depend on a single database?

### Step 4 — Analyze security and identity

Inspect:

```text
config/packages/security.yaml
src/Entity/User.php
src/Controller/SecurityController.php
```

Questions to consider:

- Is identity centralized?
- Are candidate, company, and administrator roles managed by the same security model?
- What would be affected if authentication failed?
- Which future services would depend on the same user information?

### Step 5 — Analyze business coupling

Inspect:

```text
src/Service/ApplicationManager.php
src/Controller/CandidateController.php
src/Controller/CompanyController.php
```

Look for:

- direct object references;
- synchronous method calls;
- shared database transactions;
- repository access across functional areas;
- assumptions about immediate consistency.

For every coupling observation, record:

```text
Evidence:
Architectural interpretation:
Potential impact:
Information still missing:
```

### Step 6 — Examine deployment and runtime assumptions

Use the project structure and configuration to identify the deployment model.

Questions to consider:

- How many deployable applications are present?
- Can the search feature be deployed independently?
- Can the application feature be scaled independently?
- Do all features share the same PHP runtime?
- Do all features depend on the same database?
- What is the possible impact of one critical defect?

### Step 7 — Identify missing capabilities

The baseline intentionally does not include:

- a public REST API;
- asynchronous messaging;
- independent services;
- service discovery;
- API Gateway;
- distributed tracing;
- centralized observability;
- container orchestration;
- cloud deployment configuration;
- shared object storage.

Do not automatically classify every missing element as a defect. Decide whether it is needed based on the case requirements.

---

## 6. Evidence collection table

Use the following structure in your notes:

| Observation | Evidence in the repository | Possible architectural cause | Potential impact | Confidence |
|---|---|---|---|---|
| Example: candidate application creation depends directly on Job and Candidate entities | `src/Service/ApplicationManager.php` | Cross-domain in-process coupling and shared transaction | Separation may require a new consistency strategy | Medium |

Your evidence may include:

- a file path;
- a class or method;
- a Doctrine relationship;
- a route;
- a security rule;
- a shared configuration;
- a shared database assumption;
- a deployment constraint.

---

## 7. From symptoms to architectural causes

Do not confuse a symptom with its cause.

### Example

**Symptom**

> Job search becomes slow during traffic peaks.

**Possible architectural cause**

> Search shares the same runtime and deployment unit as the rest of the application and cannot be scaled independently.

**Potential impact**

> Search load may consume resources required by unrelated functions.

**Evidence required**

> Deployment topology, runtime metrics, database queries, indexing strategy, and resource-consumption data.

The example is an analytical pattern, not a final answer. The available source code alone may not prove production behavior. Clearly identify where operational evidence is missing.

---

## 8. Dimensions to analyze

Your diagnosis must cover more than code quality.

### Technical dimension

- coupling;
- data ownership;
- transaction boundaries;
- scalability;
- availability;
- security;
- maintainability;
- deployability;
- observability.

### Organizational dimension

- team ownership;
- coordination cost;
- shared repository conflicts;
- release dependencies;
- autonomy of functional teams.

### Economic dimension

- infrastructure use;
- cost of incidents;
- cost of delayed releases;
- cost of migration;
- operational complexity.

### Business dimension

- expansion;
- partner integration;
- service-level expectations;
- time to market;
- support for new capabilities.

---

## 9. Prioritization method

For each major problem, assess:

- **Impact:** How serious is the consequence?
- **Urgency:** How soon must it be addressed?
- **Frequency:** How often does it occur?
- **Evidence:** How strong is the supporting information?
- **Uncertainty:** What is still unknown?
- **Reversibility:** How difficult would a future change be?

The goal is not to find one universally correct order. The goal is to justify your order.

---

## 10. Critical use of the AI assistant

Your initial analysis must be completed before asking an AI assistant for help.

The AI assistant may be used to:

- challenge your assumptions;
- identify a missing perspective;
- suggest counterarguments;
- clarify architectural vocabulary;
- propose questions that require additional evidence.

The AI assistant must not be used to:

- generate the complete diagnosis before you inspect the project;
- replace source-code analysis;
- make an architectural decision without context;
- provide claims that you copy without verification.

### Suggested prompt

```text
I am analyzing a Symfony 6.4 monolithic JobBoard application.
I have identified the following observations and evidence: [insert your work].
Challenge my architectural interpretation. Distinguish facts, assumptions,
possible causes, and missing evidence. Do not choose an architecture for me.
```

For at least one useful AI claim, record:

- the claim;
- the independent source or project evidence used for verification;
- your final judgment: accurate, partially accurate, out of context, or unsupported.

---

## 11. Expected individual deliverable

Prepare a concise architectural diagnosis containing the following sections.

### 1. Current situation

Describe the existing architecture and its main characteristics.

### 2. Functional and technical map

Identify the main functional areas and shared technical components.

### 3. Three priority limitations

For each limitation, provide:

- the observation;
- concrete evidence;
- the architectural cause hypothesis;
- the technical impact;
- the organizational or business impact;
- the confidence level.

### 4. Required architectural qualities

State the properties that a future architecture should improve, without selecting technologies too early.

Examples of properties include:

- independent scalability;
- fault isolation;
- interoperability;
- deployment autonomy;
- technology flexibility;
- better observability.

### 5. Missing information

List the information needed before making a final recommendation.

### 6. Critical AI reflection

Summarize one AI-assisted discussion and explain how the response was verified.

---

## 12. Preparation for the classroom session

Bring your individual diagnosis to class.

During the classroom session, the team will:

1. compare individual observations;
2. identify agreements and disagreements;
3. verify the strongest evidence;
4. consolidate the functional map;
5. prioritize the architectural tensions;
6. formulate a shared diagnosis;
7. discuss possible evolution options only after the diagnosis is validated;
8. prepare a short argument for the review committee.

The team deliverable must not be a simple merge of individual answers. The team must resolve contradictions and justify the final position.

---

## 13. Self-assessment checklist

Before submitting your individual work, verify that:

- [ ] I inspected the source code.
- [ ] I recorded precise file paths or class names.
- [ ] I distinguished facts from assumptions.
- [ ] I separated symptoms, causes, and impacts.
- [ ] I analyzed technical, organizational, economic, and business dimensions.
- [ ] I identified missing information.
- [ ] I did not assume that microservices are automatically the solution.
- [ ] I expressed required architectural qualities before technologies.
- [ ] I critically verified any AI-generated claim.
- [ ] My diagnosis is concise, structured, and evidence-based.

---

## 14. Running the project locally

The installation and demonstration commands are documented in the technical project README. The essential commands are:

```bash
composer install
php bin/console doctrine:database:create --if-not-exists
php bin/console doctrine:schema:create
php bin/console doctrine:fixtures:load --no-interaction
php -S 127.0.0.1:8000 -t public
```

The project can be analyzed without changing its code. Running it locally is useful for understanding routes, roles, screens, and runtime behavior.

---

## 15. Repository branch

The Prosit source code is available in the dedicated branch:

[Open the Prosit_01 branch](https://github.com/badi3a/AWD-Training/tree/Prosit_01)

---

## Academic integrity

The diagnosis must represent your own reasoning. Sources and AI assistance must be acknowledged. Unsupported generic recommendations will not be considered evidence-based architectural analysis.
