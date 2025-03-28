FROM openjdk:22-jdk-slim

WORKDIR /app

COPY target/task-management-api.jar app.jar

COPY src/main/resources/jwt /app/jwt

ENV JWT_ACCESS_PATH=/app/jwt/access.txt
ENV JWT_REFRESH_PATH=/app/jwt/refresh.txt

ENTRYPOINT ["java", "-jar", "app.jar"]
