FDockerfileROM maven:3.8.4-openjdk-11-slim AS builder
WORKDIR /app
COPY . /app
RUN mvn clean package

FROM openjdk:11-jre-slim
WORKDIR /app
COPY --from=builder /app/target/*.jar /app/app.jar
CMD ["java", "-jar", "app.jar"]