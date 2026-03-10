# CMPE 172 Term Project – M3 (Ready-to-Submit)

This project is a modified version of the **cmpe172-starter-demo** to implement the **M3 Web Interface + Application Skeleton** for the TechStride Interview & Certification Scheduler.

## What’s Included (M3 Checklist)

### Wireframes / Mockups
- `wireframes.pdf` (4 pages): Home, Available Slots, Booking Form, Confirmation

### Working Endpoints / Pages
- **GET /** → Home page (`home.html`)
- **GET /slots** → Available slots page (`slots.html`) backed by JDBC
- **GET /appointments/book** → Booking form (`book.html`)
- **POST /appointments/book** → Submit booking, redirects to `/confirmation`
- **GET /confirmation** → Confirmation page (`confirmation.html`)

### API (JSON)
- **GET /api/slots** → list available slots
- **GET /api/appointments** → list appointments

### Layered Architecture
Controller → Service → Repository → Database (JDBC)

## How to Run

### Option A: Maven Wrapper
```bash
./mvnw spring-boot:run
```

### Option B: Maven
```bash
mvn spring-boot:run
```

Then open:
- http://localhost:8080/

## Database

This milestone uses **SQLite** for easy local execution.

- DB file: `./techstride.db`
- Schema: `src/main/resources/schema.sql`
- Seed data: `src/main/resources/data.sql`

Spring Boot will initialize the schema and seed data on startup.

## Submission Files

- Source code (this project)
- `wireframes.pdf`
- `M3_DesignNotes.pdf`
