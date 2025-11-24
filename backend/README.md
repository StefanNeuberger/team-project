## Backend Service

### Prerequisites

- Java 21+
- Docker (optional, for MongoDB via docker compose)

### Run Locally

```bash
./mvnw spring-boot:run
```

The application starts on the default Spring Boot port (`8080`) and expects a MongoDB instance reachable at the URI configured in `application.properties`.

### Using Docker

If you prefer running MongoDB via Docker, use the provided compose file:

```bash
docker compose up -d
```

This starts a MongoDB 7 container exposed on `27017` and wired with the credentials defined in `docker-compose.yml`. Update `MONGODB_URI` if you need to point the backend at a different host or port.
