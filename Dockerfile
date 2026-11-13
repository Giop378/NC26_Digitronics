# syntax=docker/dockerfile:1

# ---------- Stage 1: build del WAR ----------
# Immagine con versione esplicita (niente :latest) per build riproducibili
FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /build

# Prima solo il pom: le dipendenze restano in cache finché il pom non cambia
COPY pom.xml .
RUN mvn -B -q dependency:go-offline

# Poi i sorgenti. I test vengono eseguiti nella pipeline CI, non qui.
COPY src ./src
RUN mvn -B -q package -DskipTests

# ---------- Stage 2: runtime su Tomcat ----------
FROM tomcat:10.1-jdk21-temurin

ENV CATALINA_OPTS="-Xms256m -Xmx512m"

# Utente non privilegiato dedicato (least privilege) e webapps pulita
RUN groupadd --system tomcat \
 && useradd --system --gid tomcat --home-dir /usr/local/tomcat --shell /usr/sbin/nologin tomcat \
 && rm -rf /usr/local/tomcat/webapps/* \
 && chown -R tomcat:tomcat /usr/local/tomcat

# COPY (non ADD) del solo artefatto finale; ROOT.war => app servita su "/"
COPY --from=build --chown=tomcat:tomcat /build/target/digitronics.war /usr/local/tomcat/webapps/ROOT.war

USER tomcat
EXPOSE 8080

# Exec form
CMD ["catalina.sh", "run"]
