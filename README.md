# StageLink Backend

Initial backend scaffold for managing performers, locations, slot booking requests, and publicly approved events.

## Stack
- Java 25
- Spring Boot 4.0.3
- PostgreSQL
- Flyway

## Package
`com.stagelink`

## Run prerequisites
- Java 25 installed
- PostgreSQL running
- Database/user (defaults):
  - DB: `stagelink`
  - User: `stagelink`
  - Password: `stagelink`

## Configuration
Environment variables used by `application.yml`:
- `DB_URL` (default: `jdbc:postgresql://localhost:5432/stagelink`)
- `DB_USERNAME` (default: `stagelink`)
- `DB_PASSWORD` (default: `stagelink`)
- `SERVER_PORT` (default: `8080`)

## Start
```bash
mvn spring-boot:run
```

Flyway executes migration `V1__initial_schema.sql` automatically at startup.

## Run with Docker
Build and start app + database:
```bash
docker compose up --build
```

Stop:
```bash
docker compose down
```

## Auth (Simple Start)
- Roles: `PLATFORM_ADMIN`, `LOCATION_ADMIN`, `PERFORMER`, `CLIENT`
- This service currently supports register/login only for `PERFORMER` and `LOCATION_ADMIN`.
- `PERFORMER` registration auto-creates a linked `performers` row.
- `LOCATION_ADMIN` registration auto-creates a linked `location_admins` row and requires `locationId`.

Register:
```bash
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "email":"artist@example.com",
    "password":"Password123!",
    "fullName":"Artist Name",
    "role":"PERFORMER",
    "performerTitle":"Band",
    "performerDescription":"Live acoustic set",
    "performerNumberOfMembers":3,
    "performerSpecialRequirements":"2 microphones"
  }'
```

Register `LOCATION_ADMIN`:
```bash
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "email":"admin-location@example.com",
    "password":"Password123!",
    "fullName":"Location Admin",
    "role":"LOCATION_ADMIN",
    "locationId":"00000000-0000-0000-0000-000000000000",
    "phoneNumber":"+40123123123"
  }'
```

Login:
```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email":"artist@example.com",
    "password":"Password123!",
    "role":"PERFORMER"
  }'
```
