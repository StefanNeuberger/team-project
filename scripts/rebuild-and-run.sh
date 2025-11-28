#!/usr/bin/env bash
set -euo pipefail

ROOT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"

RUN_TESTS=false

for arg in "$@"; do
  case "$arg" in
    -t|--test)
      RUN_TESTS=true
      ;;
    "")
      ;;
    *)
      echo "Unknown option: $arg"
      echo "Usage: $0 [-t|--test]"
      exit 1
      ;;
  esac
done

if [[ "${RUN_TESTS}" == "true" ]]; then
  echo "▶️ Running backend tests..."
  ( cd "${ROOT_DIR}/backend" && ./mvnw clean verify )
else
  echo "⚠️ Skipping backend tests (pass -t|--test to enable)"
fi

echo "🧬 Generating API client & building frontend..."
(
  cd "${ROOT_DIR}/frontend" \
  && NODE_ENV=development npm run generate:client \
  && NODE_ENV=development npm run build
)

echo "🚧 Building Docker image (frontend + backend)..."
( cd "${ROOT_DIR}" && docker build -t hanshase/teamproject:latest . )

echo "🐳 Starting MongoDB + app with docker compose..."
( cd "${ROOT_DIR}" && docker compose up --build )

