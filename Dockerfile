# Stage 1: Build the Spring Boot application
FROM maven:3.8.8-eclipse-temurin-17 AS build
WORKDIR /app

# Copy the pom.xml file to download dependencies first (allows dependency caching)
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copy the source code and build the final jar file
COPY src ./src
RUN mvn clean package -DskipTests

# Stage 2: Create a lightweight runtime container
FROM eclipse-temurin:17-jre-jammy
WORKDIR /app

# Copy the compiled jar from the build stage
COPY --from=build /app/target/rollbasedlogin-0.0.1-SNAPSHOT.jar app.jar

# Expose port 8080 (the default server.port in application.properties)
EXPOSE 8080

# Execute the application
ENTRYPOINT ["java", "-jar", "app.jar"]
