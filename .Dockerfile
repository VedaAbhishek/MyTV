# ---- Stage 1: Build the JAR ----
FROM maven:3.9.9-eclipse-temurin-21 AS builder

# Set working directory
WORKDIR /app

# Copy pom.xml and dependencies first (for layer caching)
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copy the rest of your source code
COPY src ./src

# Build the application (skip tests for faster build)
RUN mvn clean package -DskipTests

# ---- Stage 2: Run the app ----
FROM eclipse-temurin:21-jdk-jammy

# Set working directory
WORKDIR /app

# Copy the built JAR from the builder stage
COPY --from=builder /app/target/*.jar app.jar

# SQLite DB file will persist here if needed
VOLUME /app/data

# Expose the default Spring Boot port
EXPOSE 8080

# Run the app
ENTRYPOINT ["java", "-jar", "app.jar"]
