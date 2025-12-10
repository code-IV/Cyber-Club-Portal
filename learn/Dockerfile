#build
FROM maven:3.9.5-eclipse-temurin-17 AS builder
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN ./mvnw clean package -DskipTests

#run
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
COPY --from=builder /app/target/learn-0.0.1-SNAPSHOT.jar ./learn.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "learn.jar"]