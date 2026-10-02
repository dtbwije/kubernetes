# Kubernetes deployment

Create the credentials Secret before applying the workloads. Replace the example values for any shared or production cluster:

```powershell
kubectl create secret generic postgres-credentials `
  --from-literal=database=mobile-data `
  --from-literal=username=hellokube `
  --from-literal=password='replace-this-password'
```

On macOS/Linux (bash or zsh):

```bash
kubectl create secret generic postgres-credentials \
  --from-literal=database=mobile-data \
  --from-literal=username=hellokube \
  --from-literal=password='replace-this-password'
```

Apply PostgreSQL first, then the application:

```powershell
kubectl apply -f deployment/postgres.yaml
kubectl apply -f deployment/hello-kube-deployment.yaml
```

On macOS/Linux (bash or zsh):

```bash
kubectl apply -f deployment/postgres.yaml
kubectl apply -f deployment/hello-kube-deployment.yaml
```

The PostgreSQL StatefulSet stores data in a 1 GiB persistent volume claim. The application readiness probe includes the PostgreSQL health check, so app pods are not marked ready until the database is reachable. The liveness probe does not depend on PostgreSQL.

Flyway applies versioned SQL migrations from `src/main/resources/db/migration` at application startup. The initial migration creates `mobile_events`, storing each incoming event as JSONB with a generated ID and receive timestamp. PostgreSQL must be provisioned with the configured database name before the app starts; Flyway creates and updates tables inside that database, not the database itself.

`GET /mobile-data` returns the stored `payload` values as a JSON array, newest first. It returns an empty array when there are no mobile events.

## Local Docker Compose

Build the application JAR, then start the app and PostgreSQL:

```powershell
Copy-Item .env.example .env
./gradlew bootJar
docker compose up --build
```

On macOS/Linux (bash or zsh):

```bash
cp .env.example .env
./gradlew bootJar
docker compose up --build
```

The Compose app uses the `local` Spring profile and waits for PostgreSQL and Kafka to become healthy. Kafka accepts mobile-data events on the `mobile-data` topic; the consumer stores each valid JSON message in PostgreSQL. The database data is kept in a named volume. Open the app at `http://localhost:8080/h`; set `APP_PORT` if host port 8080 is already in use.

For pgAdmin or another database client running on your computer, connect with host `localhost`, port `5432`, database `mobile-data`, username `hellokube`, and the `POSTGRES_PASSWORD` value from `.env`. The password in `.env.example` is only a local development default; replace it in `.env` if desired. The same variables configure both the Postgres container and the application. A `.env` file is ignored by Git.

Send a sample event, then read the stored payloads:

```powershell
'{"deviceId":"device-1","value":42}' | docker compose exec -T kafka kafka-console-producer.sh --bootstrap-server kafka:29092 --topic mobile-data
Invoke-RestMethod http://localhost:8080/mobile-data
```

On macOS/Linux (bash or zsh):

```bash
printf '%s\n' '{"deviceId":"device-1","value":42}' | docker compose exec -T kafka kafka-console-producer.sh --bootstrap-server kafka:29092 --topic mobile-data
curl http://localhost:8080/mobile-data
```

If the existing `postgres-data` volume was initialized before changing the database name to `mobile-data`, changing `POSTGRES_DB` will not create a database in that existing volume. Create it without deleting the volume:

```powershell
docker compose exec postgres createdb -U hellokube mobile-data
```

On macOS/Linux (bash or zsh):

```bash
docker compose exec postgres createdb -U hellokube mobile-data
```

## Cloud profile

Set `SPRING_PROFILES_ACTIVE=cloud` and provide `DB_HOST`, `DB_NAME`, `DB_USERNAME`, and `DB_PASSWORD` through the cloud platform's configuration and Key Vault integration. `DB_PORT` is defined in the shared application properties and defaults to `5432`; it can be overridden with an environment variable. The Kubernetes manifests use the `postgres-credentials` Secret keys `database`, `username`, and `password`; in cloud, populate/sync those values from Key Vault instead of using the example command values. Do not commit cloud database credentials.

The cloud profile also configures Spring Kafka to use SSL. Store the Kafka keystore and truststore passwords in Key Vault and mount the corresponding PKCS12 files into the app container. Provide `KAFKA_BOOTSTRAP_SERVERS`, `KAFKA_SSL_KEYSTORE_PATH`, `KAFKA_SSL_KEYSTORE_PASSWORD`, `KAFKA_SSL_TRUSTSTORE_PATH`, and `KAFKA_SSL_TRUSTSTORE_PASSWORD` to the app from the cloud configuration/secret integration. `KAFKA_SSL_KEY_PASSWORD` is optional and defaults to the keystore password. The mounted keystore contains the client key and certificate; the truststore contains trusted broker certificates. The consumer listens on `MOBILE_DATA_TOPIC` (default `mobile-data`) using consumer group `KAFKA_CONSUMER_GROUP_ID` (default `mobile-data-processor`).

docker run --rm -it --network kubernetes_default --entrypoint /opt/kafka/bin/kafka-console-producer.sh apache/kafka:3.9.1 --bootstrap-server kafka:29092 --topic mobile-data