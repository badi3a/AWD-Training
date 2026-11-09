# candidat microservice

Spring Boot 3.5 / Java 17 service exposing a REST API for **Candidate** and **Address**
(one-to-one: `candidate.address_id -> address.id`, unique), with H2 and Swagger.

## Run
```bash
mvn spring-boot:run
```
- Swagger UI: http://localhost:8081/swagger-ui.html
- OpenAPI JSON: http://localhost:8081/v3/api-docs
- H2 console: http://localhost:8081/h2 (user `Badia`, empty password; the JDBC URL is printed in the startup log
  because `spring.datasource.url` is commented out. Uncomment it or set `jdbc:h2:mem:candidat` for a fixed URL.)

## Endpoints
| Method | Path | Description |
|---|---|---|
| GET | /api/candidates | List candidates |
| GET | /api/candidates/{id} | Get one candidate |
| POST | /api/candidates | Create candidate (address optional, created with it) |
| PUT | /api/candidates/{id} | Update candidate (and its address if given) |
| DELETE | /api/candidates/{id} | Delete candidate and its address |
| PUT | /api/candidates/{id}/address/{addressId} | Link an existing address |
| DELETE | /api/candidates/{id}/address | Unlink the address (address is kept) |
| GET | /api/addresses | List addresses |
| GET | /api/addresses/{id} | Get one address |
| POST | /api/addresses | Create address |
| PUT | /api/addresses/{id} | Update address |
| DELETE | /api/addresses/{id} | Delete address (unlinked from its candidate first) |

Errors: 400 validation, 404 not found, 409 duplicate email / address already taken (RFC 7807 JSON).

## Example
```json
POST /api/candidates
{
  "firstname": "Badia",
  "lastname": "Abouhdid",
  "email": "badia@example.com",
  "address": { "street": "Avenue Habib Bourguiba", "houseNumber": "12", "zipCode": "1001" }
}
```

## Structure
```
com.awd.candidat
├── config       OpenApiConfig (Swagger info)
├── controller   CandidateController, AddressController
├── dto          request/response records with validation + @Schema
├── entity       Candidate, Address (JPA)
├── exception    GlobalExceptionHandler, ResourceNotFoundException, ConflictException
├── mapper       CandidatMapper (entity <-> DTO)
├── repository   Spring Data JPA repositories
└── service      CandidateService, AddressService
```

Tests: `mvn test` (integration tests with MockMvc).
