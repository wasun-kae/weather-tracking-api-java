FROM openjdk:17-ea-17-jdk-slim

RUN apk --no-cache add curl
COPY target/app.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java","-jar","/app.jar"]