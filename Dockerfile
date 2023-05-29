FROM openjdk:11-jdk-slim
COPY . /app
WORKDIR /app
RUN chmod +x gradlew
RUN ./gradlew build
EXPOSE 8080
CMD ["java", "-jar", "build/libs/homebanking.jar"]
