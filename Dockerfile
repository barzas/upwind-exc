    FROM openjdk:21-jdk-slim

    WORKDIR /app
    COPY target/upwindexc-0.0.1-SNAPSHOT.jar /app/upwindexc-0.0.1-SNAPSHOT.jar

    EXPOSE 8080

    ENTRYPOINT ["java", "-jar", "upwindexc-0.0.1-SNAPSHOT.jar"] 