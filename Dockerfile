FROM openjdk:8-jdk-alpine
COPY build/libs/*.jar app.jar
RUN mkdir storage
ENTRYPOINT ["java","-jar","app.jar"]