# 🛰️ Satellite API Tests

!ВНИМАНИЕ! НА РАБОЧЕМ КОМПЬЮТЕРЕ У МЕНЯ АППАРАТНО НЕ ПОДДЕРЖИВАЕТСЯ DOCKER COMPOSE!
Как следствие, дозвониться до enpoint'ов я не могу при всём желании. Но по идее, ничего им не мешает.


Автотесты для API системы управления группировкой спутников (Space Operation Center).  
Проект написан на **Java + Gradle**, использует **JUnit 5**, **RestAssured** и **Allure Report**.

## 📋 Требования

- JDK 17 или новее
- Основной проект `satellite-constellation-system` запущен локально на порту **8080**  
  (если порт другой – измените в `TestConfig.java`)
- Gradle (можно использовать обёртку `./gradlew`)

## 🚀 Запуск тестов

```bash
./gradlew clean test
```
После выполнения тестов результаты появятся в build/allure-results/.
Генерация Allure-отчёта
Убедитесь, что установлен Allure CLI (https://docs.qameta.io/allure/#_installing_a_commandline).
Затем выполните:

```bash
./gradlew allureServe
```
Или вручную:

```bash
allure serve build/allure-results
```
Отчёт откроется в браузере автоматически.

Спутники и всё осталное првоеряются POST, GET (все/один), PUT, DELETE

Каждый метод проверяется позитивным сценарием (минимальный набор полей, ожидаемый код ответа)


Порт основного приложения задаётся в TestConfig.java:

java
RestAssured.port = 8080;   // измените при необходимости


# Проект автоматизированного тестирования Satellite API

## Ссылки на репозитории

- Основной проект: `https://github.com/unclearblast/satellite-constellation-system/tree/seminar11`
- Проект автотестов: `https://github.com/unclearblast/satellite-constellation-system/tree/autotest`

## Технологии

- Java 17
- Gradle (Kotlin DSL)
- JUnit 5
- RestAssured
- Allure Framework

## Запуск тестов и генерация отчета Allure

### 1. Клонировать проект автотестов
```bash
git clone https://github.com/unclearblast/satellite-constellation-system/tree/autotest.git
2. Убедиться, что основной сервис запущен
space-operation-center должен быть доступен по адресу http://localhost:8080

3. Выполнить тесты
bash
./gradlew clean test
4. Сгенерировать Allure отчет
bash
./gradlew allureReport
5. Открыть отчет в браузере
bash
./gradlew allureServe
