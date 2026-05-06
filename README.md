# 🛰️ Система управления спутниковой группировкой

Проект демонстрирует микросервисную архитектуру с синхронным (REST) и асинхронным потоковым (gRPC) взаимодействием.

## 📦 Состав микросервисов

| Сервис | Порт | Описание |
|--------|------|-----------|
| **space-operation-center** | 8080 (REST) | Основной сервер: управление спутниками, созвездиями, хранение данных в PostgreSQL. |
| **mission-scheduler** | 8090 (REST) | Планировщик миссий (опционально, добавлен ранее). |
| **telemetry-service** | 9091 (gRPC) | Эмулятор телеметрии: стримит каждые 2 секунды температуру трёх спутников. |

## 🔄 Взаимодействие сервисов

- `space-operation-center` → `telemetry-service` по **gRPC Server Streaming** (подписывается на поток телеметрии).
- Полученные температуры (внутри/снаружи) сохраняются в БД в таблицу `satellite`.
- При желании `telemetry-service` может получать реальный список спутников через REST от `space-operation-center` (доп. задание).

## 🚀 Запуск (Docker Compose)

Убедитесь, что установлены **Docker** и **Docker Compose**.

1. **Клонируйте репозиторий**
   ```bash
   git clone https://github.com/your-org/satellite-constellation-system.git
   cd satellite-constellation-system
Скопируйте proto-файл в основной сервер (для генерации клиентских классов)

bash
cp telemetry-service/src/main/proto/telemetry.proto space-operation-center/src/main/proto/
Сгенерируйте gRPC-классы (если не делали в IDE)

bash
cd telemetry-service && ./gradlew generateProto
cd ../space-operation-center && ./gradlew generateProto
cd ..
Соберите и запустите все сервисы

bash
docker-compose up --build
Сервисы поднимутся в одной сети space-net:

PostgreSQL на порту 5432

space-operation-center на http://localhost:8080

telemetry-service на localhost:9091 (gRPC)

Проверьте работу

Откройте http://localhost:8080/swagger-ui.html (если настроен OpenAPI)

Или через curl проверьте, что у спутников появились температуры:

bash
curl http://localhost:8080/api/satellites/1
В ответе должны быть поля insideTemperature и outsideTemperature.

🧪 Что происходит внутри?
telemetry-service генерирует случайные температуры для спутников 1, 2, 3 раз в 2 секунды.

space-operation-center при старте подключается к gRPC-потоку и слушает обновления.

Каждое обновление сохраняется в БД через JPA.

Стрим длится 2 минуты (60 итераций), затем сервер завершает поток.

🛠️ Технологии
Java 17

Spring Boot 3.2

gRPC + protobuf

Spring Data JPA / Hibernate

PostgreSQL

Docker & Docker Compose

Gradle (Kotlin DSL)

📁 Структура проекта
text
satellite-constellation-system/
├── docker-compose.yml
├── build.gradle.kts (корневой)
├── settings.gradle.kts
├── space-operation-center/
│   ├── src/main/java/com/example/spacecenter/
│   │   ├── controller/       # REST API
│   │   ├── service/          # бизнес-логика
│   │   ├── grpc/             # gRPC клиент (TelemetryConsumerService)
│   │   ├── repository/       # JPA-репозитории
│   │   └── domain/           # сущности (Satellite, Constellation...)
│   ├── src/main/proto/       # telemetry.proto (скопирован)
│   └── Dockerfile
└── telemetry-service/
    ├── src/main/java/com/example/telemetry/
    │   ├── TelemetryServiceApplication.java
    │   └── TelemetryGrpcService.java    # gRPC сервер
    ├── src/main/proto/                  # telemetry.proto (оригинал)
    ├── Dockerfile
    └── build.gradle.kts
🧠 Дополнительные задания (для саморазвития)
Реальный список спутников – заставить telemetry-service ходить в REST space-operation-center за актуальными ID спутников, чтобы не использовать мок-список.

Двусторонний стриминг – клиент может подписываться/отписываться от конкретных спутников, отправляя команды в потоке.
