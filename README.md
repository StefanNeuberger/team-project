## Team Project Workspace

This repository houses the full-stack application for the Java Bootcamp team project.

### Structure

- `backend/` – Spring Boot API connected to MongoDB.
- `frontend/` – Vite + React client.

### Dockerized Build & Run (frontend + backend + MongoDB)

1. **Build combined image** (runs frontend build, copies assets into backend, packages jar):

   ```bash
   docker build -t hanshase/teamproject:latest .
   ```

2. **Start MongoDB + app together** (uses the root `docker-compose.yml`):

   ```bash
   # Rebuild image and start both containers
   docker compose up --build

   # Subsequent runs if nothing changed
   docker compose up
   ```

   This brings up:

   - `mongo` (MongoDB 7) with credentials defined in `docker-compose.yml`
   - `app` (Spring Boot) with `SPRING_DATA_MONGODB_URI` pointing to `mongo`
   - App is exposed at http://localhost:8080

3. **Teardown**

   ```bash
   docker compose down        # stop containers
   docker compose down -v     # stop and delete Mongo volume
   ```

See `backend/README.md` for service-specific notes.

### Shortcut Script

Use `./scripts/rebuild-and-run.sh` to run the full workflow:

- `./scripts/rebuild-and-run.sh` – build image + run compose (no tests)
- `./scripts/rebuild-and-run.sh -t` – run tests (`./mvnw clean verify`) before building
- `./scripts/rebuild-and-run.sh -q` – quiet mode (suppresses docker output)
- `./scripts/rebuild-and-run.sh -t -q` – run tests + quiet docker output
