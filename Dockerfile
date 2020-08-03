FROM openjdk:11-slim
FROM maven:alpine

# image layer
WORKDIR /bot
ADD pom.xml /bot
RUN mvn verify clean --fail-never

# Image layer: with the application
COPY . /bot
RUN mvn -v
RUN mvn clean install -DskipTests
EXPOSE 8080
ADD ./target/bot.war /bot/
ENTRYPOINT ["java","-jar","/bot/bot.war"]