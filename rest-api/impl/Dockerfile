FROM eclipse-temurin:21-jdk-alpine as builder

WORKDIR /app

COPY build.gradle.kts settings.gradle.kts ./
COPY gradle ./gradle
COPY gradlew gradlew.bat ./

COPY impl ./impl
COPY api ./api
COPY db ./db

RUN chmod +x ./gradlew
RUN ./gradlew :impl:bootJar --no-daemon


FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

COPY --from=builder /app/impl/build/libs/*.jar app.jar

COPY --from=builder /app/db/src/main/resources/db/changelog /app/db/changelog

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
