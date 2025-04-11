FROM eclipse-temurin:17-jdk-alpine

WORKDIR /app

# Copy the Maven wrapper and POM file
COPY .mvn/ .mvn/
COPY mvnw pom.xml ./

# Make the Maven wrapper executable
RUN chmod +x ./mvnw

# Download dependencies with retry mechanism
RUN --mount=type=cache,target=/root/.m2 \
    ./mvnw dependency:resolve-plugins dependency:resolve \
    -DskipTests

# Copy the project source
COPY src ./src

# Package the application
RUN --mount=type=cache,target=/root/.m2 \
    ./mvnw package -DskipTests

EXPOSE 8084

# Run the application
CMD ["java", "-jar", "target/Reclamation-0.0.1-SNAPSHOT.jar"] 