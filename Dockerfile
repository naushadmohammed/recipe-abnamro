FROM azul/zulu-openjdk:25-latest AS build

WORKDIR /app

COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .
COPY src ./src

RUN ./mvnw clean package -DskipTests

FROM azul/zulu-openjdk:25-jre

WORKDIR /app
COPY --from=build /app/target/*.jar recipe.jar

ENTRYPOINT ["java","-jar","/app/recipe.jar"]