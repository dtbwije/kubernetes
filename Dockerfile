# Stage 1: build the jar (so the image builds anywhere with nothing but Docker)
FROM eclipse-temurin:17-jdk AS build
WORKDIR /workspace
COPY gradlew settings.gradle build.gradle ./
COPY gradle gradle
RUN sh ./gradlew --no-daemon dependencies > /dev/null 2>&1 || true
COPY src src
RUN sh ./gradlew --no-daemon bootJar -x test

# Stage 2: small runtime image
FROM eclipse-temurin:17-jre
WORKDIR /opt/app
COPY --from=build /workspace/build/libs/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/opt/app/app.jar"]