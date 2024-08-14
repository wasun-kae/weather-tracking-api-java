./mvnw clean install

docker compose build --no-cache
docker image prune --filter="dangling=true" --force

docker compose up