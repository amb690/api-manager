FROM openjdk:8-jdk-alpine
EXPOSE 8080
ARG JAR_FILE
COPY ${JAR_FILE} api-manager.jar
ENTRYPOINT ["java",\
"-Djava.security.egd=file:/dev/./urandom",\
"-jar",\
"/api-manager.jar"]
