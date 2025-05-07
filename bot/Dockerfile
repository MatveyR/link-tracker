FROM maven:3.9-eclipse-temurin-21 as builder

WORKDIR /app

COPY pom.xml .

RUN mvn dependency:go-offline

COPY src src

RUN mvn package -DskipTests

FROM eclipse-temurin:21-jre-jammy

RUN groupadd -r appuser && \
    useradd -r -g appuser appuser && \
    mkdir /app && \
    chown appuser:appuser /app

WORKDIR /app

COPY --from=builder --chown=appuser:appuser /app/target/*.jar app.jar

USER appuser

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
