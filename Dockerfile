# Build stage
FROM maven:3.9.6-eclipse-temurin-17 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

# Runtime stage
FROM openjdk:17-jdk-slim
WORKDIR /app
COPY --from=build /app/target/whatsapp-clone-1.0.0.jar app.jar
EXPOSE 10000
CMD ["sh", "-c", "java -Dspring.profiles.active=render -Dserver.port=$PORT -jar app.jar"]
