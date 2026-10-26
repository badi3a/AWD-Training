# Chapter 6 – Asynchronous communication with RabbitMQ

🎓 **Module: Distributed Web Applications (AWD)**  
📅 **Academic year: 2026–2027**  
🧑‍💻 **Reference code and implementation steps – pilot scenario**

> The code is shared by both language versions: its comments and log messages are in French. The log lines below are shown as they appear, with a translation.

---

## 🎯 The pilot scenario

> **A candidate applies for a job. They receive a confirmation email, but `candidature` does not wait for the email to be sent.**

In Workshop 3, `candidature` called `candidat` and `job` **synchronously** (OpenFeign): it needs their answers to decide.  
The confirmation email is different: `candidature` does **not need to wait** for the result. It **announces a fact** ("an application was submitted"), and `notification` **reacts** to it when it can.

| Question | Synchronous (OpenFeign) | Asynchronous (RabbitMQ) |
|---|---|---|
| Does the caller wait? | yes | no, it publishes and moves on |
| If the receiver is down | 503 error | the message waits in the queue |
| Does the caller know the receiver? | yes (`@FeignClient(name = "JOB")`) | no, it only knows the exchange |
| Use in the JobBoard | check the candidate and the job | announce `ApplicationSubmitted` |

---

## 🧩 Architecture

```mermaid
flowchart LR
    U["Client<br/>Postman / Swagger"]
    CA["candidature<br/>Spring Boot · :8085<br/>(producer)"]
    C["candidat :8081"]
    J["job :8082"]
    X{{"topic exchange<br/>jobboard.events"}}
    Q[["queue<br/>notification.application-submitted"]]
    N["notification<br/>FastAPI · :8084<br/>(consumer)"]
    DLX{{"direct exchange<br/>jobboard.events.dlx"}}
    DLQ[["queue<br/>…application-submitted.dlq"]]

    U -->|"POST /mic3/candidature"| CA
    CA -->|"OpenFeign (sync)"| C
    CA -->|"OpenFeign (sync)"| J
    CA -->|"publish<br/>key application.submitted"| X
    X -->|"binding application.submitted"| Q
    Q -->|"consume + ack"| N
    Q -.->|"reject(requeue=false)"| DLX
    DLX -.-> DLQ
```

| RabbitMQ element | Name | Declared by | Role |
|---|---|---|---|
| Exchange (topic, durable) | `jobboard.events` | candidature **and** notification | receives every JobBoard event |
| Routing key | `application.submitted` | candidature | describes the fact: `<entity>.<past-tense verb>` |
| Queue (durable) | `notification.application-submitted` | notification | mailbox **owned by notification** |
| Binding | `application.submitted` | notification | "send me submitted applications" |
| Dead-letter exchange (direct) | `jobboard.events.dlx` | notification | receives rejected messages |
| Dead-letter queue (DLQ) | `notification.application-submitted.dlq` | notification | messages to inspect by hand |

💡 **Design rule: the producer owns the exchange, each consumer owns its queue.**  
`candidature` knows no queue. Tomorrow, `meeting` will create its own queue bound to the same key, **without changing a single line of `candidature`**.

### The scenario step by step

```mermaid
sequenceDiagram
    autonumber
    actor U as Client
    participant CA as candidature :8085
    participant C as candidat :8081
    participant J as job :8082
    participant DB as MySQL
    participant R as RabbitMQ
    participant N as notification :8084

    U->>CA: POST /mic3/candidature {candidateId: 1, jobId: 1, motivation}
    CA->>C: GET /mic1/candidates/1 (OpenFeign)
    C-->>CA: 200 candidate
    CA->>J: GET /mic2/jobs/1 (OpenFeign)
    J-->>CA: 200 job (available = true)
    CA->>DB: INSERT candidature
    CA->>R: publish ApplicationSubmitted → jobboard.events / application.submitted
    R-->>CA: publisher confirm (ack)
    CA-->>U: 201 Created
    Note over R,N: Asynchronous: after the reply to the client
    R->>N: message delivery (queue notification.application-submitted)
    N->>N: "Application received" email
    N-->>R: ack → message removed from the queue
```

---

## 🗂️ Folder contents

```
chapitre6-rabbitmq/
├── docker-compose.yml                 RabbitMQ 4.3 + management console
├── candidature-service/               PRODUCER (Spring Boot 3.2.5, Spring Cloud 2023.0.4)
│   └── src/main/java/esprit/canditature
│       ├── messaging/                 ★ new in chapter 6
│       │   ├── RabbitConfig.java              exchange + JSON converter
│       │   ├── ApplicationSubmittedEvent.java the event (record)
│       │   └── CandidatureEventPublisher.java publishing + confirms + returns
│       ├── service/CandidatureService.java    ★ changed: publishes after the INSERT
│       └── … (entity, repository, feignClient, dto, CandidatureRestApi: Workshop 3)
└── notification-service/              CONSUMER (Python, FastAPI, aio-pika)
    ├── app/
    │   ├── main.py                            ★ lifespan: starts / stops the consumer
    │   ├── config.py                          RabbitMQ URL (RABBITMQ_URL variable)
    │   ├── schemas.py                         ★ ApplicationSubmittedEvent (message contract)
    │   ├── services/notification_service.py   ★ creates the email, ignores duplicates
    │   ├── messaging/consumer.py              ★ queue, binding, DLQ, ack / reject
    │   └── routers/notification.py            GET /api/notifications (checking)
    ├── scripts/publish_event.py               publish an event by hand
    └── tests/                                 tests without a broker (pytest)
```

In `candidature-service`, **only** `pom.xml`, `application.properties`, the `messaging/` folder and `CandidatureService.java` change compared with Workshop 3.

---

## 🛠️ Prerequisites

| Tool | Version | For |
|---|---|---|
| Docker Desktop | recent | running RabbitMQ |
| Java / Maven | 17 / 3.9 | candidature |
| Python | 3.10 or later (3.11+ recommended) | notification |
| Workshop 3 services | – | Eureka :8761, MySQL :3306, candidat :8081, job :8082 |

---

# 📝 Implementation steps

## Part A – Start RabbitMQ

### Step 1 – Start the broker

```bash
docker compose up -d
```

Open the console: **http://localhost:15672** (user `awd`, password `awd`).

| Tab | What you see |
|---|---|
| **Overview** | the broker is running, message counts |
| **Connections / Channels** | connected applications (empty for now) |
| **Exchanges** | default exchanges (`amq.direct`, `amq.topic`…) |
| **Queues and Streams** | no queue yet |

> 🔎 Port **5672** = AMQP protocol used by applications. Port **15672** = web console.

---

## Part B – The producer: `candidature` (Spring Boot)

### Step 2 – Add Spring AMQP

`pom.xml`:
```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-amqp</artifactId>
</dependency>
```

### Step 3 – Configure the connection

`application.properties`:
```properties
spring.rabbitmq.host=localhost
spring.rabbitmq.port=5672
spring.rabbitmq.username=awd
spring.rabbitmq.password=awd
spring.rabbitmq.connection-timeout=2s
```
> `connection-timeout=2s`: if RabbitMQ is down, the POST fails fast instead of hanging.

### Step 4 – Declare the exchange and the JSON format

`messaging/RabbitConfig.java`:
```java
@Configuration
public class RabbitConfig {
    public static final String EXCHANGE = "jobboard.events";
    public static final String APPLICATION_SUBMITTED_KEY = "application.submitted";

    @Bean
    public TopicExchange jobboardEventsExchange() {
        return new TopicExchange(EXCHANGE, true, false);   // durable, not auto-deleted
    }

    @Bean
    public MessageConverter jsonMessageConverter(ObjectMapper objectMapper) {
        return new Jackson2JsonMessageConverter(objectMapper);
    }
}
```
- The `TopicExchange` bean is created **on RabbitMQ** automatically on the first connection.
- Spring Boot plugs the `MessageConverter` bean into the `RabbitTemplate` automatically: Java objects leave as **JSON**, readable by Python.

### Step 5 – Write the event

`messaging/ApplicationSubmittedEvent.java`:
```java
public record ApplicationSubmittedEvent(
        String eventId,            // unique UUID: used to detect duplicates
        String eventType,          // "ApplicationSubmitted"
        Instant occurredAt,
        Long applicationId, LocalDate applicationDate,
        Long candidateId, String candidateFirstname, String candidateLastname, String candidateEmail,
        Long jobId, String jobName) { … }
```
- An **event** = a **past fact** ("application submitted"), not an order ("send an email").
- It carries the email address and the job name: `notification` does **not need to call** `candidat` or `job` back.

### Step 6 – Publish

`messaging/CandidatureEventPublisher.java` (excerpt):
```java
rabbitTemplate.convertAndSend(
        RabbitConfig.EXCHANGE,                    // to which exchange
        RabbitConfig.APPLICATION_SUBMITTED_KEY,   // with which routing key
        event);                                   // the object, converted to JSON
```
You publish to an **exchange** with a **routing key**, never directly to a queue.

### Step 7 – Publish at the right moment

`service/CandidatureService.java`, method `add`, **after** saving:
```java
Candidature saved = repository.save(new Candidature(...));

eventPublisher.publishApplicationSubmitted(saved, candidate, job);   // ★ chapter 6

return toResponse(saved, candidate, job);
```
> ⚠️ Only announce a fact **once it has happened**. If you published before the INSERT and the INSERT failed, notification would email a candidate about an application that does not exist.

---

## Part C – The consumer: `notification` (Python / FastAPI)

### Step 8 – Add aio-pika

`requirements.txt`:
```
aio-pika>=9.4,<11
```
```bash
python -m pip install -r requirements.txt
```
> aio-pika is an **asynchronous** RabbitMQ client: it fits into FastAPI's event loop.

### Step 9 – Describe the received message (the contract)

`app/schemas.py`:
```python
class ApplicationSubmittedEvent(BaseModel):
    model_config = ConfigDict(extra="ignore")   # a field added on the Java side breaks nothing
    eventId: str
    eventType: Literal["ApplicationSubmitted"]
    occurredAt: datetime
    applicationId: int
    applicationDate: date
    candidateId: int
    candidateFirstname: str
    candidateLastname: str
    candidateEmail: str
    jobId: int
    jobName: str
```
> 🔎 Field names must be **identical to the JSON keys** of the Java record (same trap as with Feign DTOs).

### Step 10 – React to the event (idempotently)

`app/services/notification_service.py` (excerpt):
```python
def handle_application_submitted(self, event):
    if event.eventId in self._processed_event_ids:   # already processed?
        return None                                  # -> ignore the duplicate
    notification = self._create(...)                 # "Application received" email
    self._send(notification)                         # simulation: log + status SENT
    self._processed_event_ids.add(event.eventId)
    return notification
```

### Step 11 – Declare the queue and consume

`app/messaging/consumer.py` (excerpt):
```python
connection = await aio_pika.connect_robust("amqp://awd:awd@localhost:5672/")
channel = await connection.channel()
await channel.set_qos(prefetch_count=10)          # at most 10 unacknowledged messages

exchange = await channel.declare_exchange("jobboard.events", aio_pika.ExchangeType.TOPIC, durable=True)
queue = await channel.declare_queue("notification.application-submitted", durable=True,
                                    arguments={...DLQ, see experiment 3...})
await queue.bind(exchange, routing_key="application.submitted")
await queue.consume(self.on_message)               # MANUAL acknowledgement

async def on_message(self, message):
    try:
        event = ApplicationSubmittedEvent.model_validate_json(message.body)
        self._service.handle_application_submitted(event)
    except Exception:
        await message.reject(requeue=False)        # unusable message -> DLQ
        return
    await message.ack()                             # processed -> RabbitMQ deletes it
```
- `declare_exchange` must use **exactly the same parameters** as Java (topic, durable), otherwise RabbitMQ refuses it.
- `ack` is sent **after** processing: if the service crashes before, RabbitMQ redelivers the message.

### Step 12 – Start the consumer with FastAPI

`app/main.py`:
```python
@asynccontextmanager
async def lifespan(app: FastAPI):
    consumer = ApplicationSubmittedConsumer(settings.rabbitmq_url, notification_service)
    consumer.start()        # background task (retries every 5 s while RabbitMQ is down)
    yield
    await consumer.stop()   # clean connection shutdown
```
> If you did Workshop 2, keep the Eureka registration in this same `lifespan`.

---

# ▶️ Part D – Run the pilot scenario

### 1. Start in this order

| # | Service | Command | Check |
|---|---|---|---|
| 1 | RabbitMQ | `docker compose up -d` | http://localhost:15672 |
| 2 | MySQL, Eureka, candidat, job | as in Workshop 3 | Eureka dashboard |
| 3 | **notification** | `cd notification-service` then `python -m uvicorn app.main:app --port 8084` | log `En écoute sur la queue 'notification.application-submitted'` (listening on the queue) |
| 4 | **candidature** | `cd candidature-service` then `mvn spring-boot:run` | CANDITATURE in Eureka |

> ⚠️ Start **notification before the first POST**: it creates the queue. A message published while no queue is bound is **lost** (see experiment 5).

### 2. Look at the topology in the console

- **Exchanges → `jobboard.events`** → *Bindings*: one row to `notification.application-submitted`, key `application.submitted`.
- **Queues**: `notification.application-submitted` (0 messages, 1 consumer) and its `.dlq`.

### 3. Apply

```bash
curl -i -X POST http://localhost:8085/mic3/candidature \
     -H "Content-Type: application/json" \
     -d '{"candidateId": 1, "jobId": 1, "motivation": "I want to join a Spring Cloud team."}'
```
Expected response: **201 Created** (application + candidate + job).

### 4. Observe the three traces

**candidature** console ("event published", "RabbitMQ confirmed the event"):
```
Événement ApplicationSubmitted publié : candidature 1 (eventId=3f2a…)
RabbitMQ a confirmé l'événement 3f2a…
```
**notification** console ("email sent to"):
```
E-mail envoyé à badia@example.com : Candidature reçue : Java Spring Boot Developer
```
**notification** API:
```bash
curl http://localhost:8084/api/notifications
```
```json
[{"recipient":"badia@example.com","subject":"Candidature reçue : Java Spring Boot Developer",
  "message":"Bonjour Badia, votre candidature n°1 au poste « Java Spring Boot Developer » a bien été reçue le 18/10/2026.",
  "type":"EMAIL","candidateId":1,"id":1,"status":"SENT","createdAt":"…","eventId":"3f2a…"}]
```

✅ **Pilot scenario passed**: one HTTP call (the POST), two synchronous calls (Feign), one asynchronous event (RabbitMQ).

---

# 🔬 Part E – Five experiments to understand

> Each new POST must target a (candidate, job) pair with no application yet, otherwise the response is **409**. If needed, delete the application with `DELETE /mic3/candidature/{id}`.

| # | Experiment | What to do | Expected result | Takeaway |
|---|---|---|---|---|
| 1 | **Temporal decoupling** | Stop notification (Ctrl+C), apply, look at the queue, restart notification | POST → **201**; the queue shows **Ready = 1**; on restart, the email goes out | The consumer can be down: the message waits. With Feign, you would get a 503. |
| 2 | **Idempotency** | `python -m scripts.publish_event --twice` | 2 messages received, **only 1** notification; log `déjà traité : ignoré` (already processed: ignored) | RabbitMQ guarantees "at least once": a consumer must handle duplicates. |
| 3 | **Poison message → DLQ** | `python -m scripts.publish_event --invalid` | log `rejeté vers la DLQ` (rejected to the DLQ); the `.dlq` holds 1 message (*Get messages* to read it) | An unreadable message must neither block the queue nor loop forever. |
| 4 | **Broker down** | `docker compose stop`, apply, then `docker compose start` | POST → **201**, but log `Échec de publication` (publish failed): **the email will never be sent** | Pilot limitation: the database and the broker are not in the same transaction → **Outbox** pattern (bonus). |
| 5 | **Unroutable message** | Stop notification, delete the queue `notification.application-submitted` in the console (*Delete queue*), apply | log `Événement non routable` (unroutable event) in candidature | Publishing with no bound queue = lost message. `publisher-returns` at least tells you. |

After experiment 5, restart notification: it recreates its queue.

---

## 🐞 Common problems

| Symptom | Likely cause | Fix |
|---|---|---|
| `Connection refused` (Java) / `RabbitMQ injoignable` (Python) | RabbitMQ is not running | `docker compose up -d` |
| `ACCESS_REFUSED - Login was refused` | wrong credentials | `awd` / `awd`, as in `docker-compose.yml` |
| `PRECONDITION_FAILED - inequivalent arg 'type' for exchange 'jobboard.events'` | Java and Python declare the exchange differently | same type (`topic`) and same `durable=True` on both sides |
| `PRECONDITION_FAILED - inequivalent arg 'x-dead-letter-exchange'` | the queue already exists with other arguments | delete the queue in the console, then restart notification |
| POST returns 201 but no email, `Événement non routable` | notification was never started: no queue | start notification once |
| `null` fields / `ValidationError` on the Python side | field names differ between the Java record and the Pydantic model | align the names (`candidateEmail`, `jobName`…) |
| `applicationDate` received as `[2026,10,18]` | JSON converter without Spring Boot's `ObjectMapper` | `new Jackson2JsonMessageConverter(objectMapper)` |
| The same message keeps coming back | `reject(requeue=True)` on a permanent error | `requeue=False` + DLQ |

---

## ✅ Automated tests (no broker)

```bash
# candidature: RabbitTemplate mocked with Mockito
cd candidature-service && mvn test

# notification: fake message (ack / reject observed)
cd notification-service && pip install -r requirements-dev.txt && pytest
```

---

## ⭐ Going further

- **meeting (Node.js)**: create the queue `meeting.application-submitted` bound to the same key with `amqplib` → two consumers, zero lines changed in candidature.
- **Retry**: retry a transient error 3 times before the DLQ (`x-death` header, or a waiting queue with a TTL).
- **Outbox**: save the event in an `outbox` table in the same transaction as the application, then publish it from that table.
- **Quorum queues**: make the queue replicated (`x-queue-type: quorum`) for a RabbitMQ cluster.

---

## 🏫 Teaching context

### Instructor: [Badia Bouhdid](https://www.linkedin.com/in/badiabouhdid)

This code was developed for the **Distributed Web Applications** module  
at **ESPRIT School of Engineering**.

