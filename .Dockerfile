# ---- Stage 1: Build the JAR ----
FROM maven:3.9.9-eclipse-temurin-21 AS builder

# Set working directory
WORKDIR /app

# Copy pom.xml first and download dependencies (for build caching)
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copy the rest of the project
COPY src ./src

# Build Spring Boot app (skip tests for faster build)
RUN mvn clean package -DskipTests


# ---- Stage 2: Create final lightweight image ----
FROM eclipse-temurin:21-jdk-jammy

# Set working directory
WORKDIR /app

# Copy the built JAR file from the builder stage
COPY --from=builder /app/target/*.jar app.jar

# Copy SQLite database file into the container
COPY MyTV.db /app/data/MyTV.db

# Ensure the directory exists for SQLite
RUN mkdir -p /app/data

# Set environment variables (optional defaults)
ENV SPRING_DATASOURCE_URL=jdbc:sqlite:/app/data/MyTV.db
ENV SPRING_DATASOURCE_DRIVER_CLASS_NAME=org.sqlite.JDBC
ENV SERVER_PORT=8080

# Expose the application port
EXPOSE 8080

# Run the app
ENTRYPOINT ["java", "-jar", "app.jar"]
