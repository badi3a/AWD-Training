# =====================================================================
#  discovery (serveur Eureka) – image Docker (Spring Boot, Java 17)
#  Construction en deux étapes (multi-stage build) :
#    1. "build"   : une image avec Maven + JDK compile le projet et produit le .jar
#    2. "runtime" : une image légère avec seulement le JRE exécute ce .jar
#  Seul le résultat de l'étape 2 est conservé dans l'image finale.
# =====================================================================

# ---------- Étape 1 : compiler avec Maven ----------
FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /app
# 1a. Copier d'abord le pom.xml et télécharger les dépendances :
#     cette couche reste en cache tant que le pom.xml ne change pas.
COPY pom.xml .
RUN mvn -q -B dependency:go-offline
# 1b. Copier le code source puis construire le jar (sans lancer les tests)
COPY src ./src
RUN mvn -q -B package -DskipTests

# ---------- Étape 2 : exécuter avec un JRE seul ----------
FROM eclipse-temurin:17-jre
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
# Limite la mémoire de la JVM : 7 conteneurs Java tournent sur le même poste
ENV JAVA_TOOL_OPTIONS="-Xmx256m"
EXPOSE 8761
ENTRYPOINT ["java", "-jar", "app.jar"]
