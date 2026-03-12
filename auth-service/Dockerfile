FROM eclipse-temurin:21-jdk-alpine

WORKDIR /app

# Copy the built jar from Maven target directory
COPY target/auth-service-*.jar app.jar

# Expose service port
EXPOSE 9100

# Standard Java entrypoint
ENTRYPOINT ["java", "-jar", "/app/app.jar"]