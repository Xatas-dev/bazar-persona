# ==========================================
# Stage 1: Build the Application
# ==========================================
FROM gradle:9.2.1-jdk21 AS builder

ARG GITHUB_TOKEN
ARG GITHUB_ACTOR

WORKDIR /app

# Copy gradle configuration first to cache dependencies
COPY build.gradle.kts settings.gradle.kts ./

COPY src ./src
RUN GITHUB_TOKEN=${GITHUB_TOKEN} GITHUB_ACTOR=${GITHUB_ACTOR} gradle bootJar

# Extract layers for optimization
# This splits the fat jar into dependencies, loader, and application code
RUN mv build/libs/bazar-persona-*.jar build/libs/application.jar
WORKDIR /app/build/libs
RUN java -Djarmode=tools -jar application.jar extract --layers --destination extracted
# ==========================================
# Stage 2: Create the Runtime Image
# ==========================================
FROM eclipse-temurin:25

WORKDIR /application

# Optimize Java memory usage for containers
# MaxRAMPercentage=75.0 means the JVM will use 75% of the container's available memory limit (e.g., 384MB of a 512MB container)
ENV JDK_JAVA_OPTIONS="-XX:MaxRAMPercentage=80.0 -XX:+UseStringDeduplication"

RUN groupadd --system spring && \
    useradd --system --gid spring --no-create-home spring && \
    chown -R spring:spring /application

# Copy the layers extracted in Stage 1
# Order matters: dependencies are least likely to change, application is most likely
COPY --from=builder /app/build/libs/extracted/dependencies/ ./
COPY --from=builder /app/build/libs/extracted/spring-boot-loader/ ./
COPY --from=builder /app/build/libs/extracted/snapshot-dependencies/ ./
COPY --from=builder /app/build/libs/extracted/application/ ./


ENTRYPOINT ["java", "-jar", "application.jar"]