# Mini Project: Weather Tracking API (Currently In-Development)

## About

REST API for weather tracking device registration and capture tracking data i.e. temperature, humidity, etc.

### Tech Stack

- [Spring Boot](https://spring.io/projects/spring-boot)
- [Maven](https://maven.apache.org/)
- [AWS DynamoDB](https://aws.amazon.com/dynamodb/)
- [Localstack](https://www.localstack.cloud/)
- [Testcontainers](https://testcontainers.com/)

### Pre-requisite

- JDK version 17
- [IntelliJ IDE (Recommended)](https://www.jetbrains.com/idea/)
- [Docker](https://www.docker.com/)

## Getting started

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