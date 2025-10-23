# Stage 1: Build Angular frontend
FROM node:20 AS frontend-build
WORKDIR /app
COPY whatsapp-frontend/package*.json ./
RUN npm install
COPY whatsapp-frontend/ ./
RUN npm run build --prod

# Stage 2: Build Spring Boot backend
FROM maven:3.9.3-eclipse-temurin-17 AS backend-build
WORKDIR /app
COPY pom.xml .
COPY src ./src
# Copy built Angular files into backend static folder
COPY --from=frontend-build /app/dist ./src/main/resources/static
RUN mvn clean package -DskipTests

# Stage 3: Runtime
FROM eclipse-temurin:17-jdk-jammy
WORKDIR /app
# Copy the Spring Boot jar
COPY --from=backend-build /app/target/*.jar app.jar
# Expose port 10000 as used in Render
EXPOSE 10000
# Run the jar
CMD ["java", "-Dspring.profiles.active=render", "-Dserver.port=10000", "-jar", "app.jar"]
