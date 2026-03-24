# TODO App — Домашнее задание по CI/CD

Spring Boot приложение с REST API для управления задачами (TODO-лист).
Настроен полный CI/CD пайплайн с автоматическим деплоем на виртуальную машину.

## Технологии

- Java 17
- Spring Boot 3.2
- Maven
- Docker (многостадийная сборка)
- GitHub Actions (CI/CD)

## REST API

| Метод  | Эндпоинт       | Описание             |
|--------|-----------------|----------------------|
| POST   | `/tasks`        | Добавить задачу      |
| GET    | `/tasks`        | Получить все задачи  |
| PUT    | `/tasks/{id}`   | Обновить задачу      |
| DELETE | `/tasks/{id}`   | Удалить задачу       |

## Пример задачи (JSON)

```json
{
  "id": 1,
  "title": "Сделать ДЗ по CI/CD",
  "completed": false,
  "createdAt": "2023-10-25"
}
```

## Локальный запуск

```bash
mvn clean package
java -jar target/todo-app-0.0.1-SNAPSHOT.jar
```

## Docker

```bash
docker build -t todo-app .
docker run -p 8081:8080 todo-app
```

## CI/CD Pipeline

Пайплайн запускается автоматически при `push` в ветку `main` и включает:

1. **build-and-test** — сборка Maven + запуск тестов
2. **docker-build** — сборка Docker-образа и публикация в Docker Hub
3. **deploy** — деплой на виртуальную машину по SSH

## Секреты GitHub (Settings → Secrets)

| Секрет             | Описание                          |
|--------------------|-----------------------------------|
| `DOCKER_USERNAME`  | Логин Docker Hub                  |
| `DOCKER_PASSWORD`  | Пароль/токен Docker Hub           |
| `VM_HOST`          | IP-адрес виртуальной машины       |
| `VM_USER`          | Имя пользователя SSH              |
| `VM_SSH_KEY`       | Приватный SSH-ключ                |
| `SSH_PORT`         | Порт SSH (например, 22)           |
