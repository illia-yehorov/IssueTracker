## Build stage
FROM gradle:jdk21-jammy AS build
WORKDIR /home/gradle/src
COPY --chown=gradle:gradle . .
RUN gradle wrapper --gradle-version 8.13
RUN ./gradlew build --no-daemon --stacktrace

## Package stage
FROM openjdk:21-ea-21-jdk-slim

ENV port=8888
ENV datasource.url=jdbc:postgresql://host.docker.internal:5432/tasktracker

RUN mkdir /app
COPY --from=build /home/gradle/src/build/libs/*.jar /app/tracker-api-1.0.0.jar
ENTRYPOINT ["java","-jar","/app/tracker-api-1.0.0.jar"]
