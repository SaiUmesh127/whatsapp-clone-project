FROM openjdk:17-jdk-slim
WORKDIR /app
COPY target/whatsapp-clone-1.0.0.jar app.jar
EXPOSE 10000
CMD ["sh", "-c", "java -Dspring.profiles.active=render -Dserver.port=$PORT -jar app.jar"]
