FROM eclipse-temurin:17-jdk-jammy

WORKDIR /Vladimir-Nachev-employees

COPY src/ src/
COPY .mvn/ .mvn/
COPY mvnw pom.xml ./

RUN ./mvnw clean install -DskipTests

CMD ["./mvnw", "spring-boot:run"]
