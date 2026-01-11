FROM eclipse-temurin:21-jre

WORKDIR /app
COPY target/quarkus-app/ /app/quarkus-app/

EXPOSE 8081
CMD ["java", "-jar", "/app/quarkus-app/quarkus-run.jar"]
