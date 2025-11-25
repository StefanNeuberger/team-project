# Frontend

React + TypeScript + Vite application with type-safe API client.

## Getting Started

### Prerequisites

- Node.js (v18+)
- Backend server running on `http://localhost:8080`

### Install Dependencies

```bash
npm install
```

### Start Development Server

```bash
npm run dev
```

The app will be available at `http://localhost:5173`

## API Client Generation

This project uses [Orval](https://orval.dev/) to generate a type-safe React Query client from the backend's OpenAPI specification.

### Usage

**1. Generate OpenAPI spec only:**

```bash
npm run generate:openapi
```

**2. Generate React Query client only (from existing openapi.json):**

```bash
npm run generate:client
```

**3. Do both in one command (recommended):**

```bash
npm run generate:api
```

### Workflow

```bash
# 1. Start backend (in backend directory)
./mvnw spring-boot:run

# 2. In frontend directory, generate everything
npm run generate:api
```

This will:

- Fetch the latest OpenAPI spec from `http://localhost:8080/v3/api-docs`
- Save it as `openapi.json`
- Generate type-safe React Query hooks in `src/api/generated/`
