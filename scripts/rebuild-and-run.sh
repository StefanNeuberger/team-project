#!/usr/bin/env bash
set -euo pipefail

ROOT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"

RUN_TESTS=false
QUIET=false

for arg in "$@"; do
  case "$arg" in
    -t|--test)
      RUN_TESTS=true
      ;;
    -q|--quiet)
      QUIET=true
      ;;
    "")
      ;;
    *)
      echo "Unknown option: $arg"
      echo "Usage: $0 [-t|--test] [-q|--quiet]"
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

echo "🚧 Building Docker image (frontend + backend)..."
if [[ "${QUIET}" == "true" ]]; then
  ( cd "${ROOT_DIR}" && docker build -t hanshase/teamproject:latest . ) > /dev/null
else
  ( cd "${ROOT_DIR}" && docker build -t hanshase/teamproject:latest . )
fi

echo "🐳 Starting MongoDB + app with docker compose..."
if [[ "${QUIET}" == "true" ]]; then
  ( cd "${ROOT_DIR}" && docker compose up --build ) > /dev/null
else
  ( cd "${ROOT_DIR}" && docker compose up --build )
fi

