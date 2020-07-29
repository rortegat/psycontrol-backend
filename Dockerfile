FROM adoptopenjdk/openjdk11:latest
COPY build/libs/*.jar app.jar
RUN mkdir storage
ENTRYPOINT ["java","-jar","app.jar"]