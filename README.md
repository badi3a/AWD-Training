<div align="center">

# Chapter 2 — Web Services and REST APIs

**Distributed Web Architecture (AWD)** · ESPRIT School of Engineering · 4th-year Computer Engineering · 2026–2027

[![Module](https://img.shields.io/badge/module-AWD-7E0C6E)](https://github.com/badi3a/AWD-Training)
[![Chapter](https://img.shields.io/badge/chapter-02-A5559A)](#chapter-2--web-services-and-rest-apis)
[![Topic](https://img.shields.io/badge/topic-REST%20APIs-CB9EC5)](#what-this-chapter-covers)
[![Video](https://img.shields.io/badge/video-YouTube-FF0000?logo=youtube&logoColor=white)](#video-lecture)

</div>

---

## Why this chapter?

In Prosit 1, JobBoard's monolith produced HTML pages for a web browser. Then new needs appeared: a mobile app for
candidates, a desktop tool for recruiters, partners who want to exchange data.
**How can one business logic serve all these clients without being copied?**

This chapter answers that question with **web services** and **REST APIs**: the back end exposes data and operations
through a well-defined contract, and each client (front end) builds its own interface on top of it.

The chapter is **technology-agnostic**: the same principles apply to any language or framework.
Their implementation with Spring Boot is covered in the dedicated module.

---

## Video lecture

<div align="center">

<a href="https://youtu.be/T0TmRpyaGoE">
  <img src="https://img.youtube.com/vi/T0TmRpyaGoE/hqdefault.jpg" alt="Web Services and REST APIs: Separating Front End and Back End" width="640">
</a>

**Web Services & REST APIs: Separating Front End and Back End** · 7 min <br>
▶ [Watch on YouTube](https://youtu.be/T0TmRpyaGoE)

</div>

---

## Learning outcomes

By the end of this chapter, students will be able to:

1. **Explain** the fundamental principles of the REST architectural style.
2. **Identify** the resources of a business application and expose them as a REST API.
3. **Distinguish** the HTTP methods (GET, POST, PUT, PATCH, DELETE) and their uses.
4. **Interpret** the most common HTTP status codes.
5. **Understand** the role of exchange formats (JSON) in distributed applications.
6. **Understand** why an API is documented with OpenAPI / Swagger.
7. **Test** a REST API with an HTTP client.

---

## What this chapter covers

| # | Section | Key ideas |
| --- | --- | --- |
| 1 | Web services | Machine-to-machine interaction over a network, interoperability, SOAP · REST · GraphQL |
| 2 | Front end and back end | The back end exposes JSON; web, mobile and desktop clients build their own UI |
| 3 | REST principles | Fielding's constraints, stateless, uniform interface, Richardson Maturity Model |
| 4 | Resources and URIs | Resource vs representation, collections, sub-resources, filtering, pagination, versioning |
| 5 | HTTP methods | CRUD mapping, safety and idempotence, PUT vs PATCH |
| 6 | Status codes | 2xx · 4xx · 5xx, choosing the right code, Problem Details (RFC 9457) |
| 7 | JSON | Value types, content negotiation (`Accept` / `Content-Type`), designing representations |
| 8 | Documenting | Why Postman is not enough, the OpenAPI contract, Swagger UI |
| 9 | Testing | What to check, curl, Postman, Swagger UI, automated tests |

---

## REST in one picture: the JobBoard API

```
 Web app (SPA)      Mobile app       Desktop app        ← front ends: presentation
        \               |               /
         \          HTTP + JSON        /
          ▼             ▼             ▼
        ┌───────────────────────────────┐
        │   JobBoard REST API           │                ← back end: business logic
        │   /api/candidates  /api/jobs  │
        │   /api/categories  /api/applications
        └───────────────┬───────────────┘
                        ▼
                    Database
```

| Method | URI | Action | Typical response |
| --- | --- | --- | --- |
| `GET` | `/api/jobs?available=true` | List the available jobs | `200 OK` |
| `GET` | `/api/jobs/1` | Read one job | `200 OK` / `404 Not Found` |
| `POST` | `/api/applications` | Apply to a job | `201 Created` + `Location` / `409 Conflict` |
| `PUT` | `/api/candidates/4` | Replace a candidate | `200 OK` |
| `PATCH` | `/api/jobs/1` | Close a job (`{"available": false}`) | `200 OK` |
| `DELETE` | `/api/applications/6` | Withdraw an application | `204 No Content` |

**Consuming an API always follows five steps:** build the request → send it → check the status code → parse the
JSON → display the data (or a clear error).

```javascript
const res = await fetch("/api/jobs?available=true", { headers: { Accept: "application/json" } });
if (!res.ok) throw new Error("HTTP " + res.status);
const jobs = await res.json();
jobs.forEach(job => console.log(job.name));
```

---

## Workshop 1 — Explore and test the JobBoard REST API

Students run the provided JobBoard back end (Spring Boot + MySQL) **without writing code**, then:

- read the API contract in **Swagger UI** before sending any request;
- run a complete scenario: create a candidate, apply to a job, update, withdraw;
- trigger and analyse the **400**, **404** and **409** errors in Problem Details format;
- critique the contract: what is documented, what is missing.

Deliverable: the completed worksheet with the requested screenshots, submitted on Blackboard.

---

## Resources in this branch

| Resource | Description |
| --- | --- |
| `chapter_2_REST_APIs.pdf` | Course slides (Beamer, 61 pages) |
| `chapter-02-rest-apis/` | LaTeX sources of the slides (`latexmk -pdf chapter_2_REST_APIs.tex`) |
| `Workshop1_REST_API_JobBoard_EN.pdf` | Workshop 1 brief |

Interactive module (SCORM) and quizzes: on the ESPRIT Blackboard space of the module.

---

## Previous and next

- ⬅ [Chapter 1 — Introduction to Distributed Architectures and SOA](https://github.com/badi3a/AWD-Training/tree/Chapter_01)
  · videos: [The Invisible Evolution of Software Architecture](https://youtu.be/iXjr6Y1VT_c) ·
  [Inside a Monolith (Prosit 1)](https://youtu.be/Q3sVymW-3G0)
- 🏠 [Module home page (main branch)](https://github.com/badi3a/AWD-Training)
- ➡ Chapter 3 — Migrating to microservices *(coming soon)*

---

## Main references

- R. T. Fielding, *Architectural Styles and the Design of Network-based Software Architectures*, PhD thesis, UC Irvine, 2000.
- IETF RFC 9110 — *HTTP Semantics* (2022) · RFC 8259 — *JSON* (2017) · RFC 9457 — *Problem Details for HTTP APIs* (2023).
- M. Massé, *REST API Design Rulebook*, O'Reilly, 2011.
- OpenAPI Initiative, *OpenAPI Specification* — spec.openapis.org.

---

## Instructor

<table>
  <tr>
    <td width="140" align="center" valign="top">
      <img src="https://avatars.githubusercontent.com/u/12083408?s=240&v=4" alt="Dr. Badia Bouhdid" width="120">
    </td>
    <td valign="top">
      <h3>Dr. Badia Bouhdid</h3>
      <p>
        Lecturer at <b>ESPRIT School of Engineering</b>, in charge of the
        <b>Distributed Web Architecture (AWD)</b> module for 4th-year Computer Engineering students.
      </p>
      <p>
        Teaching focus: software architecture, distributed systems, REST APIs and microservices,
        through problem-based learning and a single running example, <b>JobBoard</b>.
      </p>
      <p>
        <a href="mailto:badiaa.bouhdid@esprit.tn"><img src="https://img.shields.io/badge/Email-badiaa.bouhdid%40esprit.tn-7E0C6E?logo=gmail&logoColor=white" alt="Email"></a>
        <a href="https://www.linkedin.com/in/badiabouhdid"><img src="https://img.shields.io/badge/LinkedIn-badiabouhdid-0A66C2?logo=linkedin&logoColor=white" alt="LinkedIn"></a>
        <a href="https://github.com/badi3a"><img src="https://img.shields.io/badge/GitHub-badi3a-181717?logo=github&logoColor=white" alt="GitHub"></a>
      </p>
    </td>
  </tr>
</table>

<div align="center">
<sub>Enjoy the journey into distributed systems.</sub>
</div>
