FROM openjdk:17.0.2-slim-buster

RUN apt-get update && apt-get install -y curl
COPY target/app.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java","-jar","/app.jar"]