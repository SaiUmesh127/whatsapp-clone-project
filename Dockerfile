# Use official OpenJDK image
FROM openjdk:17-jdk-slim

WORKDIR /app

# Install Maven
RUN apt-get update && apt-get install -y maven

# Copy pom.xml and source code
COPY pom.xml .
COPY src ./src

# Build the project inside Docker
RUN mvn clean package -DskipTests

# Copy the generated JAR from target folder
RUN ls target/   # Optional: to verify file name
COPY target/whatsapp-clone-1.0.0.jar app.jar

# Expose port
EXPOSE 10000

# Start the app
CMD ["sh", "-c", "java -Dspring.profiles.active=render -Dserver.port=$PORT -jar app.jar"]
