FROM maven:3.9.6-openjdk-21.0.2.13-slim AS builder
WORKDIR /app
COPY . /app
RUN mvn clean package

FROM openjdk:21.0.2.13-jre-slim
WORKDIR /app
COPY --from=builder /app/target/*.jar /app/app.jar
CMD ["java", "-jar", "app.jar"]
