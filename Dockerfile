# Multi-stage build pour optimiser la taille de l'image

# Stage 1: Build
FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /app

# Copier les fichiers de configuration Maven
COPY pom.xml .
COPY src ./src

# Compiler l'application
RUN mvn clean package -DskipTests

# Stage 2: Runtime
FROM tomcat:10.1-jdk17

# Supprimer les applications par défaut de Tomcat
RUN rm -rf /usr/local/tomcat/webapps/*

# Copier le WAR généré
COPY --from=build /app/target/cinehub.war /usr/local/tomcat/webapps/ROOT.war

# Exposer le port
EXPOSE 8080

# Variables d'environnement (à override au runtime)
ENV DB_URL=jdbc:mysql://localhost:3306/cinhub_db
ENV DB_USERNAME=root
ENV DB_PASSWORD=

# Démarrer Tomcat
CMD ["catalina.sh", "run"]