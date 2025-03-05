FROM maven:amazoncorretto AS build
COPY . .
RUN mvn clean package -DskipTests

FROM openjdk:17.0-jdk-slim
COPY --from=build /target/BudgetBuddy-0.0.1-SNAPSHOT.jar BudgetBuddy.jar
EXPOSE 8080
ENTRYPOINT ["java", "jar", "BudgetBuddy.jar"]
