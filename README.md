# Satellite System — система управления спутниковыми группировками

Учебный проект на Java + Spring Boot, разработанный в рамках семинаров 1–4.  
Цель проекта — продемонстрировать чистую архитектуру, принципы SOLID и базовые подходы к тестированию
(unit / mock / integration tests).

---

## 🧭 Описание проекта

Система моделирует работу центра управления спутниковыми группировками.

Поддерживается:
- создание орбитальных группировок;
- добавление спутников разных типов;
- активация спутников;
- выполнение миссий;
- контроль состояния энергии и активности.

Проект построен с использованием **Spring Boot**, **DI**, **Lombok** и покрыт тестами.

---

## 🧱 Архитектура

Проект разделён на слои:

### Domain (доменная модель)
- `Satellite` — абстрактный спутник
- `CommunicationSatellite` — спутник связи
- `ImagingSatellite` — спутник дистанционного зондирования
- `EnergySystem` — энергетическая система спутника
- `SatelliteState` — состояние спутника
- `SatelliteConstellation` — спутниковая группировка

### Repository
- `ConstellationRepository` — хранение и доступ к группировкам

### Service
- `SpaceOperationCenterService` — бизнес-логика и сценарии управления

### Application
- `Main` — точка входа в приложение (Spring Boot)

---

## 🧪 Тестирование

Реализованы **три типа тестов**:

### Unit tests
- Проверяют логику классов в изоляции
- Создание объектов через `new`
- Пример: `ConstellationRepositoryUnitTest`

### Mock tests
- Используется Mockito
- Проверяются взаимодействия компонентов
- Пример: `ConstellationRepositoryMockTest`, `SpaceOperationCenterServiceMockTest`

### Integration tests
- Поднимается Spring Context
- Используется DI (`@Autowired`)
- Проверяется полный жизненный цикл объектов
- Пример: `ConstellationRepositoryIntegrationTest`,
  `SpaceOperationCenterServiceIntegrationTest`

---

## 📊 Покрытие тестами

Для анализа покрытия используется **JaCoCo**.

Минимальное покрытие недоменных классов: **≥ 60%**  
Фактическое покрытие превышает требуемый минимум.

---

## 🚀 Запуск проекта

### Запуск приложения
```bash
./gradlew bootRun

### Запуск тестов
```bash
./gradlew test

### Генерация отчёта JaCoCo
```bash
./gradlew test jacocoTestReport
