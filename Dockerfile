# Use a base image with Java 17
FROM eclipse-temurin:17-jdk-jammy

# Set the working directory in the container
WORKDIR /app

# Copy the application JAR file to the container
COPY target/todolist-0.0.1-SNAPSHOT.jar app.jar

# Expose the port your Spring Boot app runs on (should match application.properties)
EXPOSE 8088

# Command to run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
