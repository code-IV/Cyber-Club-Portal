#Build
FROM maven:3.9.5-eclipse-temurin-17 AS builder
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN ./mvnw clean package -DskipTests

#Run
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
COPY --from=build /app/target/gateway-0.0.1-SNAPSHOT.jar ./gateway.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "gateway.jar"]