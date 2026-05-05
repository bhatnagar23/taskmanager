# Step 1 - Build the app using Maven
FROM maven:3.9.6-eclipse-temurin-21 AS build

# Set working directory
WORKDIR /app

# Copy project files
COPY pom.xml .
COPY src ./src

# Build the jar file (skip tests for faster build)
RUN mvn clean package -DskipTests

# Step 2 - Run the app
FROM eclipse-temurin:21-jre

WORKDIR /app

# Copy the built jar from Step 1
COPY --from=build /app/target/*.jar app.jar

# Expose port
EXPOSE 8080

# Start the app
ENTRYPOINT ["java", "-jar", "app.jar"]