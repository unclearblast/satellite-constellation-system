%Смотрите LaTEX – файл. Тут его исходник:
\documentclass[12pt,a4paper]{article}
\usepackage[utf8]{inputenc}
\usepackage[T1]{fontenc}
\usepackage[russian]{babel}
\usepackage{amsmath,amssymb}
\usepackage{graphicx}
\usepackage{float}
\usepackage{booktabs}
\usepackage[margin=2.5cm]{geometry}
\usepackage{siunitx}
\usepackage{array}
\usepackage[table]{xcolor}
\usepackage{enumitem}
\usepackage{setspace}
\usepackage{tikz}

% readme.tex
% Основной документ для описания проекта Satellite Constellation System
% Компиляция: pdflatex readme.tex

\documentclass[12pt,a4paper]{article}
\usepackage[utf8]{inputenc}
\usepackage[T2A]{fontenc}
\usepackage[russian]{babel}
\usepackage{amsmath, amssymb}
\usepackage{geometry}
\geometry{top=2cm,bottom=2cm,left=2.5cm,right=2cm}
\usepackage{graphicx}
\usepackage{hyperref}
\hypersetup{
    colorlinks=true,
    linkcolor=blue,
    urlcolor=blue,
}
\usepackage{listings}
\usepackage{xcolor}
\usepackage{longtable}
\usepackage{float}
\usepackage{caption}
\usepackage{subcaption}

% Настройки листингов кода
\lstdefinestyle{javastyle}{
    language=Java,
    basicstyle=\ttfamily\small,
    keywordstyle=\color{blue}\bfseries,
    commentstyle=\color{green!60!black},
    stringstyle=\color{red},
    numbers=left,
    numberstyle=\tiny\color{gray},
    stepnumber=1,
    numbersep=5pt,
    backgroundcolor=\color{gray!10},
    frame=single,
    breaklines=true,
    breakatwhitespace=true,
    tabsize=4,
    showspaces=false,
    showstringspaces=false,
}
\lstdefinestyle{sqlstyle}{
    language=SQL,
    basicstyle=\ttfamily\small,
    keywordstyle=\color{blue},
    commentstyle=\color{green!60!black},
    numbers=left,
    numberstyle=\tiny\color{gray},
    frame=single,
    breaklines=true,
}
\lstdefinestyle{ymlstyle}{
    language=bash,
    basicstyle=\ttfamily\small,
    commentstyle=\color{green!60!black},
    numbers=left,
    numberstyle=\tiny\color{gray},
    frame=single,
    breaklines=true,
}

\title{\textbf{Satellite Constellation System} \\[0.3em]
       \large Реализация паттернов Transactional Outbox и Inbox \\[0.2em]
       \small Конструирование программного обеспечения}
\author{Сделано для МГТУ им. Баумана х БЮРО 1440 \\ Семинар 12 }
\date{\today}

\begin{document}

\maketitle
\tableofcontents
\newpage

\section{Описание проекта}

\textbf{Система управления спутниками} состоит из нескольких микросервисов, которые обмениваются асинхронными сообщениями через \textbf{Apache Kafka}.  
Основной сервис (\texttt{space-operation-center}) управляет спутниками (создание/удаление) и должен надёжно уведомлять \texttt{telemetry-service} об этих изменениях.

\subsection{Исходная проблема}
\begin{itemize}
    \item Прямая отправка события в Kafka после записи в БД не гарантирует атомарности $\rightarrow$ при сбое между записью и отправкой данные теряются.
    \item Kafka доставляет сообщения \textbf{at-least-once}, что приводит к дубликатам на стороне потребителя.
\end{itemize}

\subsection{Решение}
Внедрены два паттерна отказоустойчивости:
\begin{enumerate}
    \item \textbf{Transactional Outbox} (на стороне отправителя) – события сначала сохраняются в БД в одной транзакции с бизнес-операцией, затем фоновый процесс отправляет их в Kafka.
    \item \textbf{Inbox + идемпотентность} (на стороне получателя) – уникальный \texttt{event\_id} исключает повторную обработку дубликатов.
\end{enumerate}

\section{Архитектура и паттерны}

\subsection{Концептуальная схема}
\begin{verbatim}
[Space Operation Center]               [Telemetry Service]
       |                                        |
   +---v----+                              +---v----+
   |  БД    |                              |  БД    |
   | +----+ |                              | +----+ |
   | |Sat | |                              | |Inbx| |
   | +----+ |                              | +----+ |
   | +----+ |                              +---+----+
   | |Out | |                                  |
   | +----+ |                                  |
   +---+----+                                  |
       | (1) атомарная запись                   |
       v                                        |
[ Outbox Processor ]                            |
       | (2) отправка в Kafka                   |
       +---------------> Kafka <----------------+
                         | (3) чтение
                         v
                 [ Kafka Consumer ]
                         | (4) проверка Inbox
                         v
                 (5) обработка + запись в Inbox
\end{verbatim}

\subsection{Используемые паттерны}
\begin{longtable}{|p{0.25\textwidth}|p{0.35\textwidth}|p{0.35\textwidth}|}
\hline
\textbf{Паттерн} & \textbf{Где применяется} & \textbf{Назначение} \\
\hline
Transactional Outbox & \texttt{space-operation-center} & Гарантирует, что событие будет отправлено в Kafka тогда и только тогда, когда бизнес-операция успешно зафиксирована в БД. \\
\hline
Outbox Scheduler & \texttt{space-operation-center} & Фоновый процесс, читающий таблицу \texttt{outbox} и отправляющий события в Kafka. При успехе помечает запись как \texttt{SENT}. \\
\hline
Inbox & \texttt{telemetry-service} & Таблица \texttt{inbox} хранит ID уже обработанных событий для обеспечения идемпотентности. \\
\hline
Idempotent Consumer & \texttt{telemetry-service} & Перед обработкой события проверяется наличие \texttt{event\_id} в \texttt{inbox} – если есть, событие игнорируется. \\
\hline
\end{longtable}

\section{Структура проекта}
Ниже представлено полное дерево каталогов.  
\textcolor{red}{\textbf{✅}} – новые/изменённые файлы (относительно базовой реализации Kafka), остальные – неизменная часть.

\begin{verbatim}
satellite-constellation-system/
+-- build.gradle.kts
+-- settings.gradle.kts
+-- docker-compose.yml
+-- space-operation-center/
|   +-- build.gradle.kts                     ✅
|   +-- src/main/
|       +-- java/com/example/spacecenter/
|       |   +-- SpaceOperationCenterApplication.java  ✅
|       |   +-- service/
|       |   |   +-- SatelliteService.java              ✅
|       |   |   +-- OutboxScheduler.java               ✅
|       |   +-- domain/outbox/Outbox.java              ✅
|       |   +-- dto/SatelliteEvent.java                ✅
|       |   +-- repository/OutboxRepository.java       ✅
|       +-- resources/
|           +-- application.yml                        ✅
|           +-- schema.sql                             ✅
+-- telemetry-service/
|   +-- build.gradle.kts                     ✅
|   +-- src/main/
|       +-- java/com/example/telemetry/
|       |   +-- dto/SatelliteEvent.java                ✅
|       |   +-- domain/inbox/Inbox.java                ✅
|       |   +-- repository/InboxRepository.java        ✅
|       |   +-- listener/SatelliteEventListener.java   ✅
|       |   +-- service/SatelliteStorageService.java   ✅
|       +-- resources/
|           +-- application.yml                        ✅
|           +-- schema.sql                             ✅
+-- mission-scheduler/                       (без изменений)
\end{verbatim}

\section{Реализация Transactional Outbox}

\subsection{Таблица \texttt{outbox} (PostgreSQL)}
\begin{lstlisting}[style=sqlstyle, caption=DDL для outbox]
CREATE TABLE outbox (
    id BIGSERIAL PRIMARY KEY,
    aggregate_id VARCHAR(255) NOT NULL,
    event_type VARCHAR(50) NOT NULL,
    payload JSONB NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),
    status VARCHAR(20) NOT NULL DEFAULT 'PENDING'
);
CREATE INDEX idx_outbox_status ON outbox(status);
\end{lstlisting}

\subsection{Сохранение события в той же транзакции}
\begin{lstlisting}[style=javastyle, caption=SatelliteService.java (фрагмент)]
@Transactional
public Satellite createSatellite(Satellite satellite) {
    Satellite saved = satelliteRepository.save(satellite);
    createOutboxEvent(saved.getId().toString(), "CREATED", saved);
    return saved;
}

private void createOutboxEvent(String aggregateId, String eventType, Object payload) {
    String eventId = UUID.randomUUID().toString();
    SatelliteEvent event = new SatelliteEvent(eventId, aggregateId, eventType, payload);
    String json = objectMapper.writeValueAsString(event);
    outboxRepository.save(new Outbox(aggregateId, eventType, json));
}
\end{lstlisting}

\subsection{Планировщик отправки}
\begin{lstlisting}[style=javastyle, caption=OutboxScheduler.java]
@Scheduled(fixedDelayString = "${outbox.scheduler.fixed-delay:5000}")
@Transactional
public void processOutbox() {
    List<Outbox> pending = outboxRepository.findByStatus(PENDING);
    for (Outbox record : pending) {
        try {
            kafkaTemplate.send("satellite-events", record.getAggregateId(), record.getPayload());
            outboxRepository.updateStatus(record.getId(), SENT);
        } catch (Exception e) {
            log.error("Failed to send, will retry later", e);
        }
    }
}
\end{lstlisting}

\section{Реализация Inbox (идемпотентность)}

\subsection{Таблица \texttt{inbox}}
\begin{lstlisting}[style=sqlstyle, caption=DDL для inbox]
CREATE TABLE inbox (
    event_id VARCHAR(255) PRIMARY KEY,
    aggregate_id VARCHAR(255) NOT NULL,
    event_type VARCHAR(50) NOT NULL,
    processed_at TIMESTAMP NOT NULL DEFAULT NOW()
);
CREATE INDEX idx_inbox_aggregate ON inbox(aggregate_id);
\end{lstlisting}

\subsection{Consumer с проверкой дубликатов}
\begin{lstlisting}[style=javastyle, caption=SatelliteEventListener.java]
@KafkaListener(topics = "satellite-events", groupId = "telemetry-group")
@Transactional
public void handleSatelliteEvent(SatelliteEvent event) {
    if (inboxRepository.existsById(event.getEventId())) {
        log.info("Duplicate event {} ignored", event.getEventId());
        return;
    }
    inboxRepository.save(new Inbox(event.getEventId(), event.getAggregateId(), event.getEventType()));
    
    // бизнес-логика
    if ("CREATED".equals(event.getEventType())) {
        satelliteStorageService.addSatellite(event.getAggregateId());
    } else if ("DELETED".equals(event.getEventType())) {
        satelliteStorageService.removeSatellite(event.getAggregateId());
    }
}
\end{lstlisting}
\textbf{Важно}: \texttt{enable-auto-commit: false} и \texttt{@Transactional} гарантируют атомарность записи \texttt{inbox} и обработки.

\section{Запуск и тестирование}

\subsection{Предварительные требования}
\begin{itemize}
    \item Docker \& Docker Compose
    \item Java 17
    \item Gradle (или использование встроенного wrapper)
\end{itemize}

\subsection{Файл \texttt{docker-compose.yml}}
\begin{lstlisting}[style=ymlstyle, caption=docker-compose.yml]
version: '3'
services:
  postgres-space:
    image: postgres:15
    environment:
      POSTGRES_DB: space_center
      POSTGRES_USER: postgres
      POSTGRES_PASSWORD: password
    ports:
      - "5432:5432"
  postgres-telemetry:
    image: postgres:15
    environment:
      POSTGRES_DB: telemetry
      POSTGRES_USER: postgres
      POSTGRES_PASSWORD: password
    ports:
      - "5433:5432"
  zookeeper:
    image: confluentinc/cp-zookeeper:latest
    environment:
      ZOOKEEPER_CLIENT_PORT: 2181
    ports:
      - "2181:2181"
  kafka:
    image: confluentinc/cp-kafka:latest
    depends_on:
      - zookeeper
    environment:
      KAFKA_BROKER_ID: 1
      KAFKA_ZOOKEEPER_CONNECT: zookeeper:2181
      KAFKA_ADVERTISED_LISTENERS: PLAINTEXT://localhost:9092
      KAFKA_OFFSETS_TOPIC_REPLICATION_FACTOR: 1
    ports:
      - "9092:9092"
\end{lstlisting}

\subsection{Запуск сервисов}
\begin{verbatim}
# Терминал 1
cd space-operation-center
./gradlew bootRun

# Терминал 2
cd telemetry-service
./gradlew bootRun
\end{verbatim}

\subsection{Проверка}
Создайте спутник через REST API \texttt{space-operation-center}:
\begin{verbatim}
curl -X POST http://localhost:8080/satellites \
  -H "Content-Type: application/json" \
  -d '{"name":"Sputnik-1","state":"ACTIVE"}'
\end{verbatim}
Через несколько секунд в логах \texttt{telemetry-service} появится:
\begin{verbatim}
Processed event <uuid> for satellite 1
Satellite 1 added to telemetry storage
\end{verbatim}
При ручном повторе отправки (например, перезапуск планировщика) Kafka может доставить дубликат, но \texttt{inbox} отфильтрует его:
\begin{verbatim}
Duplicate event <uuid> ignored
\end{verbatim}

\section{Диаграмма потока событий}
\begin{figure}[H]
\centering
\begin{verbatim}
Client -> SOC: POST /satellites
SOC -> DB: BEGIN TX
SOC -> DB: INSERT satellite
SOC -> DB: INSERT outbox (PENDING)
SOC -> DB: COMMIT TX
SOC --> Client: 200 OK

loop Every 5 sec
    Scheduler -> DB: SELECT pending outbox
    Scheduler -> Kafka: send(event)
    Scheduler -> DB: UPDATE status -> SENT
end

Kafka -> Telemetry: deliver event
Telemetry -> InboxDB: SELECT exists(event_id)
alt event_id not found
    Telemetry -> InboxDB: INSERT into inbox
    Telemetry -> Telemetry: process business logic
    Telemetry -> Kafka: commit offset
else event_id already exists
    Telemetry -> Telemetry: ignore duplicate
end
\end{verbatim}
\caption{Sequence diagram в текстовом виде}
\end{figure}

\section{Заключение}
Реализованные паттерны \textbf{Transactional Outbox} и \textbf{Inbox} обеспечивают:
\begin{itemize}
    \item \textbf{Гарантированную доставку} – событие не будет потеряно даже при сбое сразу после записи в БД.
    \item \textbf{Идемпотентность} – повторные доставки (at-least-once) не приводят к двойной обработке.
    \item \textbf{Согласованность} – между \texttt{space-operation-center} и \texttt{telemetry-service} сохраняется актуальное состояние спутников.
\end{itemize}
Такой подход рекомендован для production-систем с асинхронным взаимодействием и высокими требованиями к надёжности.

\section*{Дополнительные материалы}
\begin{itemize}
    \item \href{https://microservices.io/patterns/data/transactional-outbox.html}{Pattern: Transactional Outbox}
    \item \href{https://www.enterpriseintegrationpatterns.com/patterns/messaging/IdempotentConsumer.html}{Idempotent Consumer}
    \item \href{https://docs.spring.io/spring-kafka/reference/html/}{Spring Kafka Reference}
\end{itemize}

\vfill
\begin{center}
    \textcopyright\ Разработано в рамках курса «Конструирование программного обеспечения» \\

\end{center}

\end{document}
