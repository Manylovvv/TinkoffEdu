FROM openjdk:19
ARG JAR=scrapper/target/scrapper-1.0-SNAPSHOT.jar
WORKDIR /app/scrapper
COPY ${JAR} scrapper.jar
ENTRYPOINT ["java", "-jar", "scrapper.jar"]