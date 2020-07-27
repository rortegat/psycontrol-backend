#Stage 1 - Gradle
FROM gradle:6.1.1 AS gradle
WORKDIR /app
COPY ./ /app
RUN gradle build

#Stage 2 - OpenJDK
FROM openjdk:8-jdk-alpine
COPY --from=gradle /app/build/libs/*.jar app.jar
RUN mkdir storage
ENTRYPOINT ["java","-jar","app.jar"]