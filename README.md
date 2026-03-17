# BookingClimbingGym

A multi-module Spring Boot REST API for managing climbing gym reservations. Users can discover gyms, purchase passes,
and book sessions, while gym managers can register their venues, manage capacity, and view bookings.

---

## Table of Contents

- [Features](#features)
- [Architecture](#architecture)
- [Tech Stack](#tech-stack)
- [Testing](#testing)
- [ToDo](#todo)

---

## Features

- **User Management** — Register, login, and manage accounts with JWT-based authentication
- **Email Verification** — Verify email before registration via a time-limited ticket
- **Gym Registration** — Managers can register gyms with business and bank account verification
- **Gym Search** — Search gyms by keyword with pagination
- **Pass Management** — Gyms offer passes with configurable uses and validity periods
- **Booking System** — Book gym sessions using purchased passes
- **Booking Cancellation** — Cancel bookings within the gym's cancellation notice window
- **Crowdedness Check** — Query how busy a gym will be at a given date and time
- **Manager View** — Gym owners can view all bookings for their gym

---

## Architecture

The project follows a **Clean Architecture** / **Layered Architecture** pattern with strict separation of concerns
across four layers:

```
┌─────────────────────────────────────────┐
│                   API                   │  Controllers, DTOs, Response wrappers
├─────────────────────────────────────────┤
│                Services                 │  Business logic, orchestration
├─────────────────────────────────────────┤
│                 Domain                  │  Entities, repository interfaces
├─────────────────────────────────────────┤
│                  Infra                  │  Repository implementations, external adapters
└─────────────────────────────────────────┘
         Common (shared utilities across all layers)
```

Each module only depends on lower-level modules. The `domain` module defines repository interfaces that `infra`
implements — the service layer never touches persistence directly.

---

## Tech Stack

| Category         | Technology                                  |
|------------------|---------------------------------------------|
| Language         | Java 17                                     |
| Framework        | Spring Boot 4.0.3                           |
| Build Tool       | Gradle 9.3.1 (multi-module)                 |
| Authentication   | JJWT 0.12.6 (JWT tokens)                    |
| Password Hashing | Spring Security Crypto (BCrypt)             |
| Validation       | Jakarta Validation / Hibernate Validator    |
| Email            | Spring Boot Mail (SMTP)                     |
| Testing          | JUnit 5, Mockito, Spring Boot Test, MockMvc |
| Code Generation  | Lombok                                      |
| Serialization    | Jackson                                     |
| Storage          | In-memory (ConcurrentHashMap)               |

---

## Testing

The project has both **unit tests** and **integration tests**.

### Unit Tests (`services` module)

Use JUnit 5 + Mockito with all dependencies mocked. A fixed `Clock` is used for deterministic time-based assertions.

| Test Class          | Coverage                                                     |
|---------------------|--------------------------------------------------------------|
| `UserServiceTests`  | Registration, login, token validation, password mismatch     |
| `GymServiceTests`   | Gym registration, pass booking, pass validation, crowdedness |
| `EmailServiceTests` | Sending codes, verifying codes, invalid/expired codes        |

### Integration Tests (`api` module)

Use Spring Boot Test with `MockMvc` and real in-memory repositories wired together.

| Test Class             | Coverage                                               |
|------------------------|--------------------------------------------------------|
| `UserControllerTests`  | `POST /app/users`, `GET /app/users/auto-login`         |
| `GymControllerTests`   | `POST /app/gyms`, business verification endpoints      |
| `EmailControllerTests` | `POST /app/email/code`, `POST /app/email/verification` |

## ToDo

### Features to Add

- [ ] Add Gym photos
- [ ] Implement pass purchase logic

### Technical Improvements

- [ ] Add logic in the API layer to return different response codes based on domain exceptions *(Should the API layer
  even be aware of domain exceptions?)*
- [ ] Connect to a database
- [ ] Add unit tests for the Domain layer
- [ ] Implement `SmtpEmailService`
- [ ] Add QR check-in flow
- [ ] Set up Docker / development environment

### Refactoring

- [ ] Email verification codes don't need to be stored permanently — consider storing them in Redis (or similar) for TTL
  management instead of the database

### Testing

- [ ] Find a way to test the payment flow

### Open Technical Questions

- [ ] How can we validate a Gym business? (e.g. verifying legitimacy)