# TechStride Interview & Certification Scheduler - Final Project

## Run

```bash
./mvnw spring-boot:run
```

If the wrapper is not executable on macOS/Linux:

```bash
chmod +x mvnw
./mvnw spring-boot:run
```

Open: http://localhost:8080/

## Demo pages

- Home UI: http://localhost:8080/
- Available slots: http://localhost:8080/slots
- Booking form: http://localhost:8080/appointments/book
- Appointments JSON: http://localhost:8080/api/appointments
- Slots JSON: http://localhost:8080/api/slots
- Health: http://localhost:8080/health
- Metrics: http://localhost:8080/metrics

## Main final project features

- Java + Spring Boot application
- SQL database through JDBC only
- SQLite local database
- Controller -> Service -> Repository -> Database layering
- Booking flow
- Optimistic locking with version column
- UNIQUE slot constraint to prevent double-booking
- Mock external notification service
- Logging around booking and notification behavior
- Health endpoint
- Metrics endpoint
- Styled Thymeleaf UI

## API booking test

```bash
curl -i -X POST http://localhost:8080/api/appointments/book-and-notify \
  -H "Content-Type: application/json" \
  -d '{"candidateId":1,"slotId":1001,"serviceId":1}'
```

Run again with the same slot and a different candidate to show double-booking prevention:

```bash
curl -i -X POST http://localhost:8080/api/appointments/book-and-notify \
  -H "Content-Type: application/json" \
  -d '{"candidateId":2,"slotId":1001,"serviceId":1}'
```

Expected result: the first request succeeds, and the second returns HTTP 409 Conflict.
