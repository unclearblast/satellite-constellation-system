# 🛰 Satellite Constellation System

Микросервисная система управления спутниковыми группировками с автоматическим выполнением миссий по расписанию.

Проект состоит из **двух сервисов**:

- 🛰 `space-operation-center` — основной сервис управления спутниками (Seminar 7)
- ⏰ `mission-scheduler` — сервис-планировщик миссий (Seminar 8)

---

## 🚀 Архитектура


+------------------------+ HTTP (REST) +------------------------+
| Mission Scheduler | -----------------------> | Space Operation Center |
| (port 8081) | | (port 8080) |
+------------------------+ +------------------------+


---

## 🛰 Space Operation Center (Seminar 7)

### 📌 Возможности

- ➕ Добавление спутников
- 🚀 Запуск миссий
- 📊 Получение состояния системы
- ❌ Вывод спутника из эксплуатации

### 📡 API

| Метод | Endpoint | Описание |
|------|--------|----------|
| POST | `/api/add-satellites` | Добавить спутники |
| POST | `/api/missions` | Выполнить миссию |
| GET  | `/api/overview` | Получить состояние |
| DELETE | `/api/constellations/{constellation}/satellites/{satellite}` | Удалить спутник |

### 📘 Swagger


http://localhost:8080/swagger-ui.html


---

## ⏰ Mission Scheduler (Seminar 8)

### 📌 Возможности

- 📥 Читает миссии из `application.yml`
- ⏱ Планирует выполнение через CRON
- 🌐 Вызывает основной сервис через REST
- 🧾 Логирует выполнение
- ⚠️ Обрабатывает ошибки (не падает)

---

## ⚙️ Конфигурация

### `mission-scheduler/src/main/resources/application.yml`

```yaml
server:
  port: 8081

app:
  space-center-service:
    url: "http://localhost:8080/api"
    missions:
      - targetType: CONSTELLATION
        constellationName: "GeoStationary"
        cron: "0 0 */6 * * *"

      - targetType: SINGLE_SATELLITE
        constellationName: "LowOrbit"
        satelliteName: "Sat-1"
        cron: "0 30 8 * * MON"
⏱ CRON формат (Spring)
секунда минута час день месяц день_недели
Примеры
Cron	Описание
0 */1 * * * *	каждую минуту
0 0 */6 * * *	каждые 6 часов
0 30 8 * * MON	каждый понедельник 8:30
▶️ Запуск
1. Запуск основного сервиса
cd space-operation-center
./gradlew bootRun
2. Запуск планировщика
cd mission-scheduler
./gradlew bootRun
🧪 Проверка

После запуска scheduler:

Executing mission...
Mission success...

Если основной сервис выключен:

Mission failed: Connection refused

👉 Это нормально — сервис устойчив к ошибкам.

🧱 Технологии
Java 17+
Spring Boot 3.x
Spring Web
RestClient (Spring 6.1+)
Spring Scheduling
Lombok
OpenAPI / Swagger
🧠 Особенности реализации
✔ ConfigurationProperties

Все миссии хранятся в YAML:

@ConfigurationProperties(prefix = "app.space-center-service")
✔ Планировщик
taskScheduler.schedule(task, new CronTrigger(cron));
✔ HTTP клиент
restClient.post()
    .uri("/missions")
    .body(request)
    .retrieve()
