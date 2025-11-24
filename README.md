## Team Project Workspace

This repository houses the full-stack application for the Java Bootcamp team project.

### Structure

- `backend/` – Spring Boot API connected to MongoDB.
- `frontend/` – Vite + React client.

### Getting Started

1. Clone the repo and move into the project directory.
2. Install dependencies per service:
   - Backend uses the Maven wrapper (`./mvnw`).
   - Frontend uses Node 18+ with `npm install`.

### Running the Backend

```bash
cd backend
./mvnw spring-boot:run
```

See `backend/README.md` for Docker instructions and more details.

### Running the Frontend

```bash
cd frontend
npm install
npm run dev
```

### Tests

- Backend: `cd backend && ./mvnw test` (runs Spring Boot tests with Testcontainers)

### Docker Support

The backend ships with a `docker-compose.yml` for MongoDB. Start it from `backend/` using `docker compose up -d` and set `MONGODB_URI` if you need to override defaults.
