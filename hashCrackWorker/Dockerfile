FROM openjdk:17-jdk-alpine
FROM maven:3.9.9-eclipse-temurin-17 AS build
WORKDIR /app

COPY pom.xml .
RUN mvn dependency:go-offline

COPY src /app/src
RUN mvn clean package -DskipTests

FROM openjdk:17-jdk-alpine
WORKDIR /app

COPY --from=build /app/target/hashCrackWorker-0.0.1.jar /app/app.jar

CMD ["java", "-jar", "/app/app.jar"]
