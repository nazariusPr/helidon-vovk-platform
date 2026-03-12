FROM eclipse-temurin:21-jdk-alpine

WORKDIR /app

# Copy the built jar
COPY target/inspection-service-*.jar app.jar

# Expose service port
EXPOSE 9300

# Standard Java entrypoint
ENTRYPOINT ["java", "-jar", "/app/app.jar"]