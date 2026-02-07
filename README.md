# 🛰 Satellite Constellation Management System

## 📌 Описание проекта
Данный проект представляет собой консольную систему управления спутниковыми группировками,
реализованную на языке **Java** с использованием **Spring Boot** и принципов
**объектно-ориентированного проектирования (ООП)** и **SOLID**.

Проект был выполнен в рамках **3 семинаров**, каждый из которых последовательно улучшал архитектуру системы.

---

## 🎯 Цели проекта
- Продемонстрировать практическое применение принципов SOLID
- Реализовать Dependency Inversion Principle (DIP) с помощью Spring Boot
- Построить расширяемую архитектуру, готовую к подключению базы данных
- Отделить бизнес-логику от инфраструктурного кода

---

## 🧱 Используемые технологии
- Java 21
- Spring Boot 3
- Gradle (Kotlin DSL)
- UML (draw.io)

---

## 🧠 Реализованные принципы SOLID

### ✅ SRP — Single Responsibility Principle
Каждый класс отвечает только за одну зону ответственности:
- `EnergySystem` — управление энергией
- `SatelliteState` — управление состоянием
- `Satellite` — абстракция поведения спутника
- `SpaceOperationCenterService` — сценарии работы с группировками

### ✅ OCP — Open/Closed Principle
Добавление новых типов спутников возможно без изменения существующего кода,
достаточно унаследоваться от `Satellite`.

### ✅ LSP — Liskov Substitution Principle
Все наследники `Satellite` корректно заменяют базовый тип и используются полиморфно.

### ✅ DIP — Dependency Inversion Principle
- `Main` не создаёт зависимости напрямую
- Все сервисы и репозитории создаются Spring-контейнером
- `SpaceOperationCenterService` получает `ConstellationRepository` через конструктор

---

## 🗂 Архитектура проекта

```text
src/main/java/seminars
├── Main.java
├── domain
│   ├── constellation
│   │   └── SatelliteConstellation.java
│   └── satellite
│       ├── Satellite.java
│       ├── CommunicationSatellite.java
│       ├── ImagingSatellite.java
│       ├── EnergySystem.java
│       └── SatelliteState.java
├── repository
│   └── ConstellationRepository.java
└── service
    └── SpaceOperationCenterService.java
