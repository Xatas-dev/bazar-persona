# ==========================================
# Stage 1: Extract layered jar (no build)
# ==========================================
FROM eclipse-temurin:25-jre AS builder

WORKDIR /builder

ARG JAR_FILE=build/libs/application.jar
COPY ${JAR_FILE} application.jar

RUN java -Djarmode=tools -jar application.jar extract --layers --destination extracted

# ==========================================
# Stage 2: Runtime image
# ==========================================
FROM eclipse-temurin:25-jre

WORKDIR /application

ENV JDK_JAVA_OPTIONS="-XX:MaxRAMPercentage=80.0 -XX:+UseStringDeduplication"

RUN groupadd --system spring && \
    useradd --system --gid spring --no-create-home spring && \
    chown -R spring:spring /application

COPY --from=builder /builder/extracted/dependencies/ ./
COPY --from=builder /builder/extracted/spring-boot-loader/ ./
COPY --from=builder /builder/extracted/snapshot-dependencies/ ./
COPY --from=builder /builder/extracted/application/ ./

USER spring
ENTRYPOINT ["java", "-jar", "application.jar"]