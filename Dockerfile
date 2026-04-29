FROM maven:3.9.15-eclipse-temurin-25 AS build
COPY . .
RUN mvn clean package "-Dmaven.test.skip=true"


FROM eclipse-temurin:25
COPY --from=build /target/demoProject-0.0.1-SNAPSHOT.jar demo.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","demo.jar"]