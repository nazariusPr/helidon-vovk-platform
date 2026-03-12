FROM eclipse-temurin:21-jdk-alpine

WORKDIR /app

# Copy the built jar
COPY target/equipment-service-*.jar app.jar

# Expose service port
EXPOSE 9200

# Standard Java entrypoint
ENTRYPOINT ["java", "-jar", "/app/app.jar"]