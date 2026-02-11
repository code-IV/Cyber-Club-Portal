#!/bin/bash
set -e

# ---- ENV ----
export DATABASE_URL=jdbc:postgresql://localhost:5433/multi_service_db
export POSTGRES_USER=appuser_dev
export POSTGRES_DB=multi_service_db
export POSTGRES_PASSWORD=dev_secure_password
export JWT_SECRET=asdfghjkl-asdfghjkl-asdfghjkl-asdfghjkl
export INTERNAL_GATEWAY_SECRET=test-secret

# ---- WAIT FOR POSTGRES ----
echo "Waiting for Postgres..."
until pg_isready -h localhost -p 5433 -U "$POSTGRES_USER" -d "$POSTGRES_DB"; do
  echo -n "."
  sleep 2
done
echo "Postgres is ready!"

# ---- MIGRATIONS ----
SCHEMAS=(identity challenge community learn portal)

for schema in "${SCHEMAS[@]}"; do
  echo "Migrating schema: $schema"

  # Generate temporary flyway config
  TMP_CONF=$(mktemp)
  sed "s/{schema}/$schema/g" infra/flyway.conf.template > "$TMP_CONF"

  # Run Flyway CLI
  ./tools/flyway/flyway -configFiles="$TMP_CONF" migrate

  rm "$TMP_CONF"
done

echo "All migrations completed!"

# ---- START SERVICES ----
SERVICES=(identity gateway portal learn community challenge)

for service in "${SERVICES[@]}"; do
  echo "Starting $service..."
  (cd "$service" && ./mvnw spring-boot:run &) 
done

echo "All services started!"
echo "Use 'jobs' to see running services or [  pkill -f "spring-boot:run"  ] to stop all."
