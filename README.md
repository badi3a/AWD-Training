# 🌐 Prosit 1 — Diagnostic architectural de l’application JobBoard

**Module : Applications Web Distribuées (AWD)**  
**Chapitre 1 : Introduction aux architectures distribuées**  
**Année universitaire : 2026–2027**  
**Public : 4e année — Cycle Ingénieur en Informatique**  
**École : ESPRIT — École d’Ingénieurs**

---

## 🎯 Objectifs du Prosit

Ce Prosit constitue la première situation-problème du module **Applications Web Distribuées**. Il invite les étudiants à analyser l’application **JobBoard** et à établir un diagnostic argumenté de son architecture monolithique dans un contexte de croissance.

À l’issue du Prosit, les étudiants seront capables de :

- expliquer l’évolution des architectures logicielles vers les architectures distribuées modernes ;
- distinguer les architectures **monolithique**, **N-tiers**, **SOA** et **Microservices** ;
- identifier les avantages et les limites d’une architecture monolithique ;
- analyser les besoins métier et les contraintes techniques qui peuvent justifier une évolution architecturale ;
- reconnaître les caractéristiques fondamentales d’un système distribué ;
- expliquer le rôle des services Web dans l’interopérabilité entre applications ;
- formuler un diagnostic architectural fondé sur des arguments techniques et métier.

---

## 📚 Prérequis

- notions de base en **programmation orientée objet** ;
- compréhension du patron architectural **MVC** ;
- notions générales sur les applications Web ;
- capacité à lire un diagramme d’architecture ou un diagramme de classes ;
- aptitude à travailler en équipe et à rechercher des informations techniques fiables.

---

## 🧩 Situation-problème

**JobBoard** est une plateforme de recrutement qui centralise plusieurs fonctionnalités métier, notamment :

- la gestion des candidats ;
- la gestion des entreprises ;
- la publication des offres d’emploi ;
- le suivi des candidatures ;
- l’organisation des entretiens ;
- l’envoi de notifications.

La première version de JobBoard repose sur une **architecture monolithique** : les fonctionnalités sont développées, déployées et exploitées comme une seule application, avec une base de données partagée.

Avec l’augmentation du nombre d’utilisateurs, des fonctionnalités, des intégrations et des équipes de développement, plusieurs difficultés peuvent apparaître :

- temps de maintenance plus important ;
- couplage entre les modules ;
- déploiement global pour une modification locale ;
- difficulté à faire évoluer ou dimensionner une fonctionnalité indépendamment ;
- risque qu’une défaillance affecte l’ensemble de l’application ;
- coordination plus complexe entre les équipes ;
- augmentation de la dette technique.

### ❓ Problématique centrale

> **L’architecture monolithique de JobBoard reste-t-elle adaptée aux nouveaux besoins de l’application, ou faut-il envisager une évolution vers une architecture distribuée ?**

L’objectif n’est pas de conclure automatiquement que les Microservices constituent la meilleure solution. L’équipe doit identifier les problèmes réels, analyser les contraintes et défendre une recommandation proportionnée au contexte.

---

## 🔎 Travail demandé

Les étudiants travaillent en équipe pour produire un diagnostic architectural structuré.

### 1. Comprendre l’existant

- identifier les principales fonctionnalités de JobBoard ;
- repérer les modules métier et leurs dépendances ;
- déterminer les éléments déployés ensemble ;
- identifier les données partagées ;
- décrire les échanges entre l’interface, la logique métier et la base de données.

### 2. Analyser l’architecture monolithique

- présenter ses avantages dans le contexte initial du projet ;
- identifier ses limites face à la croissance ;
- distinguer les problèmes fonctionnels, techniques, organisationnels et opérationnels ;
- associer chaque problème à un élément observable ou à un scénario concret.

### 3. Comparer les styles architecturaux

Comparer les options suivantes :

- architecture monolithique ;
- monolithe modulaire ;
- architecture N-tiers ;
- architecture orientée services, **SOA** ;
- architecture Microservices.

La comparaison doit considérer au minimum :

- le couplage ;
- le déploiement ;
- la maintenabilité ;
- la scalabilité ;
- la disponibilité ;
- la gestion des données ;
- la complexité opérationnelle ;
- l’organisation des équipes.

### 4. Formuler une recommandation

- déterminer si l’architecture actuelle peut être conservée, restructurée ou progressivement distribuée ;
- justifier la décision en fonction des besoins de JobBoard ;
- proposer une première représentation de l’architecture cible ;
- identifier les risques et compromis associés à la proposition.

---

## 💡 Questions directrices

- Quelles sont les caractéristiques qui permettent de qualifier JobBoard de monolithe ?
- Quels avantages cette architecture apporte-t-elle au démarrage du projet ?
- Quels signes montrent qu’une architecture devient difficile à faire évoluer ?
- Une séparation logique en couches implique-t-elle nécessairement une architecture distribuée ?
- Quelles fonctionnalités de JobBoard pourraient avoir des besoins de charge différents ?
- Quelles conséquences un déploiement global peut-il avoir sur les délais et les risques ?
- Comment une base de données partagée influence-t-elle le couplage des modules ?
- Quels nouveaux problèmes apparaissent lorsqu’une application devient distribuée ?
- Un monolithe modulaire pourrait-il répondre à une partie des difficultés ?
- Quels éléments factuels faudrait-il mesurer avant de recommander une migration ?

---

## 📦 Livrables attendus

Chaque équipe doit remettre :

1. **Une présentation synthétique** du diagnostic architectural ;
2. **Un schéma de l’architecture actuelle** de JobBoard ;
3. **Un tableau comparatif** des architectures étudiées ;
4. **Une recommandation argumentée** et adaptée au contexte ;
5. **Un schéma d’évolution possible**, sans entrer dans l’implémentation détaillée ;
6. **Une liste des sources consultées**.

Le livrable doit mettre en évidence le raisonnement de l’équipe. Une recommandation non justifiée ou fondée uniquement sur la popularité d’une technologie n’est pas suffisante.

---

## ✅ Critères de réussite

- compréhension correcte de l’architecture existante ;
- distinction claire entre architecture en couches et architecture distribuée ;
- identification pertinente des avantages et limites du monolithe ;
- comparaison équilibrée des styles architecturaux ;
- prise en compte des besoins métier et des contraintes techniques ;
- recommandation cohérente, progressive et argumentée ;
- qualité et lisibilité des schémas ;
- participation équilibrée des membres de l’équipe ;
- utilisation de sources fiables et correctement citées.

---

## 🛠️ Outils recommandés

Le Prosit n’impose aucun outil particulier. Les équipes peuvent utiliser :

- [Microsoft PowerPoint](https://www.microsoft.com/microsoft-365/powerpoint) ou un outil équivalent pour la présentation ;
- [diagrams.net](https://www.diagrams.net/) pour les schémas d’architecture ;
- [PlantUML](https://plantuml.com/) ou [Mermaid](https://mermaid.js.org/) pour les diagrammes textuels ;
- un espace collaboratif, un dépôt Git ou un document partagé pour organiser le travail ;
- des sources académiques, documentations techniques et ouvrages spécialisés pour étayer l’analyse.

---

## 📂 Ressources pédagogiques

- [Support du Chapitre 1 — Introduction aux architectures distribuées](https://esprit.blackboard.com)
- [Guide enseignant du Prosit 1](https://github.com/badi3a/AWD-Training/blob/Prosit_01/Prosit.pdf)


---

## 🤝 Organisation du travail en équipe

- constituer une équipe de **3 à 5 étudiants** ;
- répartir les rôles : animation, recherche, analyse, modélisation et restitution ;
- confronter les hypothèses avant de sélectionner une solution ;
- conserver une trace des sources et des décisions ;
- préparer une restitution où chaque membre peut expliquer le diagnostic et la recommandation.

---


## 📝 Conseil méthodologique

> **Ne commencez pas par choisir une technologie. Commencez par caractériser le problème, les contraintes et les qualités attendues.**

Une architecture distribuée peut améliorer l’autonomie, le déploiement et la scalabilité, mais elle introduit également des coûts : communication réseau, pannes partielles, cohérence des données, observabilité, sécurité et complexité opérationnelle.

**Bon travail et bon diagnostic architectural !**
---

## 🏫 Cadre pédagogique

### Enseignante

- [Badia Bouhdid](https://www.linkedin.com/in/badiabouhdid)

Ce Prosit est proposé dans le cadre du module **Applications Web Distribuées** à l’[École d’Ingénieurs ESPRIT](https://www.esprit.tn). Il s’inscrit dans une démarche d’**apprentissage par problèmes**, dans laquelle les étudiants analysent une situation, recherchent les connaissances nécessaires, confrontent leurs propositions et construisent une réponse argumentée.


