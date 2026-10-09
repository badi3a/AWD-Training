<div align="center">

# Distributed Web Applications (AWD)

**Course materials and problem-based learning activities**

ESPRIT School of Engineering · 4th-year Computer Engineering Program · Academic year 2026–2027

[![Module](https://img.shields.io/badge/module-AWD-7E0C6E)](#about-the-module)
[![Academic year](https://img.shields.io/badge/academic%20year-2026--2027-A5559A)](#about-the-module)
[![Approach](https://img.shields.io/badge/approach-problem--based%20learning-CB9EC5)](#how-we-learn-prosits)
[![Language](https://img.shields.io/badge/language-English-555555)](#)

</div>

---

## About the module

Modern applications are rarely a single program running on a single server. They are used from browsers, phones and
desktop tools, they talk to partner systems, and they must keep working while thousands of people use them at the same
time. **Distributed Web Applications** explores how such systems are designed: how their parts are separated, how they
communicate, and which trade-offs come with each architectural choice.

Throughout the module, a single running example, **JobBoard**, an online recruitment platform, helps connect the
concepts from one chapter to the next.

---

## Repository organisation

Each chapter lives in **its own branch**. The `main` branch is the entry point: it presents the module and points to
the chapters as they become available.

| [`chapter-01`](https://github.com/badi3a/AWD-Training/tree/chapter_01) | Introduction to Distributed Architectures and SOA | Course slides, Prosit 1 | Available |


To open a chapter, select its branch on GitHub, or from a terminal:

```bash
git clone https://github.com/badi3a/AWD-Training.git
cd AWD-Training
git switch chapter-01
```

---

## Chapter 1 — Introduction to Distributed Architectures and SOA

<p align="center">
  <img src="https://github.com/badi3a/AWD-Training/blob/Chapter_01/slide1.png" alt="Chapter 1 title slide: Introduction to Distributed Architectures and SOA" width="720">
</p>

The first chapter sets the scene for the whole module. It follows the story of software architectures, from the
mainframe era to today's cloud-native systems, and shows that each new style appeared as an answer to the limits of the
previous one.

By the end of the chapter, students will be able to:

- explain how software architectures have evolved over time;
- distinguish the main architectural styles: **monolith**, **N-tier**, **SOA** and **microservices**;
- identify the limits of a monolithic architecture as an application grows;
- understand the basic principles of distributed systems;
- explain the role of Web services in making applications work together;
- describe the main ideas behind a **RESTful** architecture.

The message of the chapter is simple: there is no "best" architecture in absolute terms, only architectures that fit a
given context.

---

## How we learn: Prosits

Alongside the lectures, the module relies on **problem-based learning**. A *Prosit* is a realistic situation in which
students, working in small teams, identify the problem, search for the knowledge they need, test their hypotheses and
build a well-argued answer, with the guidance of a tutor.

Each Prosit follows three steps:

1. **Opening session** — discover the situation, define the keywords, state the problem and the hypotheses.
2. **Self-directed work** — investigate, analyse and gather evidence as a team.
3. **Closing session** — present the findings, compare viewpoints and build a shared synthesis.

---

## Prosit 1 — JobBoard: a monolith facing the multi-device challenge

JobBoard started as a simple Web application and has become a success. Users now want to reach it from their phones,
business clients ask for a desktop tool, and the platform must handle more and more activity. The team behind it
realises that the application, built as a single block, finds it hard to keep up with these changes.

In this Prosit, students take on the role of an audit team. Their mission is to:

- understand what the application does, who uses it and how;
- explain why its current architecture makes change difficult;
- identify the limits it meets as new needs appear;
- propose a gradual, well-justified direction for its evolution.

The goal is not to jump to a fashionable solution, but to reason carefully: understand the problem first, weigh the
benefits and the costs of each option, and recommend what truly fits JobBoard's situation.

> **A piece of advice:** do not start by choosing a technology. Start by understanding the problem, the constraints
> and the qualities that matter.

The full Prosit brief is available in the [`chapter-01`](https://github.com/badi3a/AWD-Training/blob/Chapter_01/AWD_Prosit1.pdf) branch.

---

## Learning resources

- Course slides and interactive modules: available on the ESPRIT Blackboard space of the module.
- Chapter materials and Prosit briefs: in the corresponding chapter branch of this repository.

---

## Instructor

**Dr. Badia Bouhdid** — ESPRIT School of Engineering
[badiaa.bouhdid@esprit.tn](mailto:badiaa.bouhdid@esprit.tn) · [LinkedIn](https://www.linkedin.com/in/badiabouhdid)

This repository supports the **Distributed Web Applications** module at
[ESPRIT School of Engineering](https://www.esprit.tn).

<div align="center">
<sub>Enjoy the journey into distributed systems.</sub>
</div>
