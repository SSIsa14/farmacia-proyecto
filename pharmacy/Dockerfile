# Etapa de construcción con Maven
FROM maven:3.9.6-eclipse-temurin-17 AS build

# Crear directorio y copiar el código
WORKDIR /app
COPY . .

# Ejecutar los tests y construir el JAR
RUN mvn clean package 

# Etapa final: solo el JAR limpio
FROM eclipse-temurin:17-jdk

WORKDIR /app

# Copiar solo el JAR desde la etapa anterior
COPY --from=build /app/target/pharmacy-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8080

CMD ["java", "-jar", "app.jar"]
