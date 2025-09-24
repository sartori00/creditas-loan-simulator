FROM maven:3.9.6-eclipse-temurin-21-jammy AS builder
WORKDIR /app
COPY pom.xml .
COPY src ./src

RUN mvn clean install -DskipTests

FROM eclipse-temurin:21-jre-jammy AS final
WORKDIR /app
COPY --from=builder /app/target/*.jar app.jar
EXPOSE 8090

ENTRYPOINT ["java", "-jar", "app.jar"]
