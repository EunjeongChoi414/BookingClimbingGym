FROM gradle:8-jdk17 AS build
WORKDIR /app
COPY . .
RUN gradle :api:bootJar --no-daemon

FROM eclipse-temurin:17-jre
WORKDIR /app
COPY --from=build /app/api/build/libs/*.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]
