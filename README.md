# Mini Project: Weather Tracking API (currently in-development)

## About

REST API for weather tracking device registration and capture tracking data i.e. temperature, humidity, etc.

### Tech Stack

- [Spring Boot](https://spring.io/projects/spring-boot)
- [SpringDoc OpenAPI](https://springdoc.org/)
- [DynamoDB](https://aws.amazon.com/dynamodb/)
- [Localstack](https://www.localstack.cloud/)
- [Maven](https://maven.apache.org/)
- [Lombok](https://projectlombok.org/)
- [JUnit](https://junit.org/junit5/)
- [Mockito](https://site.mockito.org/)
- [Testcontainers](https://testcontainers.com/)

## Getting started

### Pre-requisite

- [JDK version 17](https://openjdk.org/)
- [Docker](https://www.docker.com/)
- [IntelliJ IDE (Recommended)](https://www.jetbrains.com/idea/)

### Run Unit Testing

This will execute unit test files (`*Test.java`)

```
./mvnw test
```

### Run Integration Testing

This will execute integration test files (`*IT.java`) with DynamoDB on Localstack Testcontainer

```
./mvnw integration-test
```

### Build Application Artifact

This will create `app.jar` in `target` directory

```
./mvnw clean install
```

### Run Application with DynamoDB on Localstack

This will build an artifact and Docker image to run with `docker-compose.yml` file

```
./start.sh
```

### Open API specification

Go to `http://localhost:8080/swagger-ui/index.html` after run `start.sh` script
