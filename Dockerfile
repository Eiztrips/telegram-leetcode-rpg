FROM eclipse-temurin:25-jdk as builder
LABEL authors="Eiztrips"
WORKDIR /app
COPY . .
RUN chmod +x ./gradlew
RUN ./gradlew build -x test

FROM eclipse-temurin:25-jre
RUN addgroup --system app && adduser --system --ingroup app app
WORKDIR /app
COPY --from=builder /app/build/libs/*.jar app.jar
USER app
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]