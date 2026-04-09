# 🛰 Satellite Constellation System

Микросервисная система управления спутниковыми группировками с автоматическим выполнением миссий по расписанию.  
Проект полностью контейнеризован и запускается через **Docker Compose**.

- **Сеть**: пользовательская Docker-сеть `satellite-net`
- **DNS внутри сети**: `server` → контейнер основного сервиса
- **Конфигурация**: адрес сервера передаётся через переменную окружения `SERVER_URL`

## 🛰 Space Operation Center (сервер)

### 📌 Возможности

- ➕ Добавление спутников
- 🚀 Запуск миссий
- 📊 Получение состояния системы
- ❌ Вывод спутника из эксплуатации

### 📡 API

| Метод   | Endpoint                                                   | Описание                |
|---------|------------------------------------------------------------|-------------------------|
| POST    | `/api/add-satellites`                                      | Добавить спутники       |
| POST    | `/api/missions`                                            | Выполнить миссию        |
| GET     | `/api/overview`                                            | Получить состояние      |
| DELETE  | `/api/constellations/{constellation}/satellites/{satellite}` | Удалить спутник         |

### 📘 Swagger UI

После запуска доступен по адресу:  
👉 [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)

## ⏰ Mission Scheduler (клиент)

### 📌 Возможности

- 📥 Читает список миссий из `application.yml`
- ⏱ Планирует их выполнение по CRON-выражениям
- 🌐 Вызывает API основного сервиса по HTTP
- 🧾 Логирует результаты
- ⚠️ Устойчив к ошибкам соединения (не прекращает работу)

## 🐳 Запуск через Docker Compose (Семинар 9)

### 📦 Состав контейнеров

| Сервис          | Порт на хосте | Внутренний порт | Имя контейнера        |
|-----------------|---------------|-----------------|-----------------------|
| space-center    | 8080          | 8080            | space-center-server   |
| mission-service | 8081          | 8081            | mission-scheduler     |

### ⚙️ Переменные окружения

| Переменная      | Сервис         | Описание                           | Значение по умолчанию    |
|-----------------|----------------|------------------------------------|--------------------------|
| `SERVER_PORT`   | server         | Порт, на котором слушает сервер    | `8080`                   |
| `SERVER_PORT`   | mission-service| Порт, на котором слушает клиент    | `8081`                   |
| `SERVER_URL`    | mission-service| URL основного сервиса              | `http://localhost:8080`  |

### ▶️ Запуск

1. Убедитесь, что Docker и Docker Compose установлены.
2. Из корня проекта выполните:

```bash
docker-compose up --build

3. Дождитесь сообщений о готовности (healthcheck сервера проходит за ~10 секунд).

\Структура проекта (после Docker-обновления)
text
satellite-constellation-system/
├── docker-compose.yml
├── .dockerignore
├── space-operation-center/
│   ├── Dockerfile
│   ├── .dockerignore
│   ├── build.gradle
│   ├── src/...
│   └── src/main/resources/application.yml
├── mission-scheduler/
│   ├── Dockerfile
│   ├── .dockerignore
│   ├── build.gradle
│   ├── src/...
│   └── src/main/resources/application.yml
└── README.md

⚙️ Локальный запуск (без Docker)
Если вы хотите запустить сервисы локально (например, для отладки):

1. Space Operation Center
bash
cd space-operation-center
./gradlew bootRun
2. Mission Scheduler
bash
cd mission-scheduler
./gradlew bootRun
При локальном запуске убедитесь, что в application.yml планировщика указан корректный URL сервера (по умолчанию http://localhost:8080/api).

⏱ CRON формат (Spring)
Spring использует шестипозиционный CRON:
секунда минута час день месяц день_недели

Примеры
Cron	Описание
0 */1 * * * *	каждую минуту
0 0 */6 * * *	каждые 6 часов
0 30 8 * * MON	каждый понедельник в 8:30
