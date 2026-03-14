# ========== Стадия сборки ==========
FROM maven:3.8.6-eclipse-temurin-17 AS builder
WORKDIR /app
COPY pom.xml .
# Загружаем зависимости отдельным слоем (кэширование Docker)
RUN mvn dependency:resolve
COPY src ./src
# Собираем проект, пропуская тесты (они уже прошли на этапе CI)
RUN mvn package -DskipTests

# ========== Стадия выполнения ==========
FROM eclipse-temurin:17-jre-jammy
WORKDIR /app
# Копируем JAR-файл из стадии builder
COPY --from=builder /app/target/*.jar app.jar

# Открываем порт 8081
EXPOSE 8081

# Healthcheck — проверяем, что приложение отвечает
HEALTHCHECK --interval=30s --timeout=3s --start-period=10s --retries=3 \
  CMD curl -f http://localhost:8080/actuator/health || exit 1

# Запускаем приложение
ENTRYPOINT ["java", "-jar", "app.jar"]
