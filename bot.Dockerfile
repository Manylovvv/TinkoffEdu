FROM openjdk:19
ARG JAR=bot/target/bot-1.0-SNAPSHOT.jar
WORKDIR /app/bot
COPY ${JAR} bot.jar
ENTRYPOINT ["java", "-jar", "bot.jar"]
