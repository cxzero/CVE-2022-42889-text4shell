FROM openjdk:11-jdk-slim

RUN apt update && apt install maven python netcat wget curl -y

WORKDIR /opt/text4shell-poc

# Compile
COPY pom.xml ./
COPY src/ ./src/

RUN mvn clean package -DskipTests

EXPOSE 8080
CMD ["java", "-jar", "target/spring-boot-0.0.1-SNAPSHOT.jar"]