# Étape 1 : construction du JAR avec Maven
FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

# Étape 2 : exécution du JAR
FROM eclipse-temurin:17
WORKDIR /app
COPY --from=build /app/target/reservation.jar app.jar
EXPOSE 8089
ENTRYPOINT ["java", "-jar", "app.jar"]