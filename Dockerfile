# ==============================================================================
# Stage 1: Build Stage
# ==============================================================================
FROM maven:3.9.9-eclipse-temurin-21-alpine AS builder

WORKDIR /app

# Cache Maven dependencies
COPY pom.xml .
RUN mvn dependency:go-offline -B || true

# Copy source code and build executable jar
COPY src ./src
RUN mvn clean package -DskipTests

# ==============================================================================
# Stage 2: Runtime Stage
# ==============================================================================
FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

# Create a non-root system user and group for security
RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring

# Copy built jar from the builder stage
COPY --from=builder --chown=spring:spring /app/target/*.jar app.jar

# JVM options optimized for container environment (respects VPS cgroup limits)
ENV JAVA_OPTS="-XX:+UseContainerSupport -XX:MaxRAMPercentage=75.0" \
    PORT=8080

EXPOSE 8080

ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]

