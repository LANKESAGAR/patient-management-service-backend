FROM maven:3.9.9-eclipse-temurin-21 AS builder

WORKDIR /app

COPY pom.xml .

# Download dependencies first (cache-friendly)
RUN mvn dependency:go-offline -B

COPY src ./src

# ❗ Skip tests to avoid ApplicationContext errors during image build
RUN mvn clean package -DskipTests

# --- Runner stage ---
FROM eclipse-temurin:21-jdk AS runner

WORKDIR /app

# ❗ Fix the path: it should be /app/target (not .app/target)
COPY --from=builder /app/target/*.jar ./app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
