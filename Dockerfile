# ---- Build stage ----
FROM eclipse-temurin:25-jdk AS build
WORKDIR /app

# Download dependencies first so they are cached between builds
COPY mvnw pom.xml ./
COPY .mvn .mvn
RUN chmod +x mvnw && ./mvnw -q dependency:go-offline

COPY src src
RUN ./mvnw -q package -DskipTests

# ---- Runtime stage ----
FROM eclipse-temurin:25-jre
WORKDIR /app

RUN useradd --system --no-create-home spring
USER spring

COPY --from=build /app/target/*.jar app.jar

# Database credentials and CORS origins come from runtime environment variables:
# DB_URL, DB_USERNAME, DB_PASSWORD, CORS_ALLOWED_ORIGINS
ENV SPRING_PROFILES_ACTIVE=prod
EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
