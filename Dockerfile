FROM openjdk:17-ea-17-jdk-slim
COPY target/app.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java","-jar","/app.jar"]