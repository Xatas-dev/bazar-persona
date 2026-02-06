# ==========================================
# Stage 1: Build the Application
# ==========================================
FROM gradle:9.2.1-jdk21 AS builder

WORKDIR /app

# Copy gradle configuration first to cache dependencies
COPY build.gradle.kts settings.gradle.kts ./
RUN gradle dependencies --no-daemon
COPY src ./src
RUN gradle bootJar --no-daemon

# Extract layers for optimization
# This splits the fat jar into dependencies, loader, and application code
RUN mv build/libs/bazar-space-*.jar build/libs/application.jar
WORKDIR /app/build/libs
RUN java -Djarmode=tools -jar application.jar extract --layers --destination extracted

# ==========================================
# Stage 2: Create the Runtime Image
# ==========================================
FROM eclipse-temurin:25

WORKDIR /application

# Optimize Java memory usage for containers
# MaxRAMPercentage=75.0 means the JVM will use 75% of the container's available memory limit
# G1GC for better memory management and low pause times
# UseCompressedOops reduces memory footprint by 20-30%
# UseStringDeduplication saves memory on duplicate strings
ENV JDK_JAVA_OPTIONS="-Dspring.aot.enabled=true \
    -XX:MaxRAMPercentage=75.0 \
    -XX:InitialRAMPercentage=50.0 \
    -XX:+UseG1GC \
    -XX:MaxGCPauseMillis=200 \
    -XX:+UseCompressedOops \
    -XX:+UseCompressedClassPointers \
    -XX:+UseStringDeduplication \
    -XX:+ExitOnOutOfMemoryError"

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