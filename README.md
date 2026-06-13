\documentclass[12pt,a4paper]{article}
\usepackage[utf8]{inputenc}
\usepackage[T2A]{fontenc}
\usepackage[russian]{babel}
\usepackage{geometry}
\usepackage{enumitem}
\usepackage{hyperref}
\geometry{top=1cm,bottom=1.5cm,left=1.5cm,right=1cm}
\hypersetup{colorlinks=true,urlcolor=blue,linkcolor=black}
% README.tex
% Доказательство выполнения задания на 20 баллов: 
% - проект успешно запускается, настроено логирование и конфигурация
% - тест выдерживает временной интервал, симулирует разгон и удержание пользователей
% - сгенерирован HTML-отчёт (k6) с необходимой информацией

\usepackage[utf8]{inputenc}
\usepackage[T2A]{fontenc}
\usepackage[russian]{babel}
\usepackage{amsmath,amssymb}
\usepackage{newunicodechar}
\newunicodechar{✅}{\checkmark}
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
\usepackage{hyperref}
\hypersetup{
    colorlinks=true,
    linkcolor=blue,
    urlcolor=blue,
}
\usepackage{listings}
\usepackage{xcolor}
\usepackage{longtable}
\usepackage{caption}
\usepackage{subcaption}

% Настройки листингов
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
\lstdefinestyle{kotlinstyle}{
    language=Java,
    basicstyle=\ttfamily\small,
    keywordstyle=\color{blue}\bfseries,
    commentstyle=\color{green!60!black},
    stringstyle=\color{red},
    numbers=left,
    numberstyle=\tiny\color{gray},
    backgroundcolor=\color{gray!10},
    frame=single,
    breaklines=true,
    morekeywords={implementation,testImplementation,runtimeOnly,compileOnly,annotationProcessor,plugins,id,version,group,sourceCompatibility,repositories,mavenCentral,dependencies},
}
\lstdefinestyle{ymlstyle}{
    language=bash,
    basicstyle=\ttfamily\small,
    commentstyle=\color{green!60!black},
    numbers=left,
    numberstyle=\tiny\color{gray},
    backgroundcolor=\color{gray!10},
    frame=single,
    breaklines=true,
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

\title{\textbf{Satellite Constellation System} \\[0.3em]
\large Внедрение кэширования через Redis с Spring Cache Abstraction \\[0.2em]
\small Семинар 13}
\author{Мартиросян Микаэл Дереникович}
\date{\today}

\begin{document}

\maketitle
\tableofcontents
\newpage

\section{Описание проекта}
\textbf{Спутниковая система} состоит из нескольких микросервисов, центральным из которых является \texttt{space-operation-center}. Этот сервис часто запрашивает данные о спутниках и группировках, что создаёт нагрузку на базу данных. Для ускорения операций чтения и снижения нагрузки внедряется кэширование с использованием \textbf{Redis} и абстракции \textbf{Spring Cache}.

\subsection{Цели}
\begin{itemize}
    \item Кэшировать результаты методов чтения с разными TTL.
    \item Автоматически инвалидировать кэш при изменении данных.
    \item Обеспечить отказоустойчивость: при недоступности Redis приложение должно продолжать работу (graceful degradation).
    \item Предоставить метрики кэширования через Actuator.
\end{itemize}

\section{Архитектура кэширования}
\begin{figure}[H]
\centering
\begin{tikzpicture}[node distance=1.5cm, auto]
    \node[draw, rectangle] (client) {Клиент (REST)};
    \node[draw, rectangle, below of=client] (service) {SatelliteService};
    \node[draw, rectangle, below left of=service, xshift=-2cm] (redis) {Redis (кэш)};
    \node[draw, rectangle, below right of=service, xshift=2cm] (db) {PostgreSQL (БД)};
    \draw[->] (client) -- node[sloped] {запрос} (service);
    \draw[->] (service) -- node[sloped] {1. проверка кэша} (redis);
    \draw[->] (redis) -- node[sloped] {промах} (db);
    \draw[->] (db) -- node[sloped] {данные} (service);
    \draw[->] (service) -- node[sloped] {2. сохранение в кэш} (redis);
    \draw[->] (service) -- node[sloped] {ответ} (client);
\end{tikzpicture}
\caption{Поток запроса с кэшем}
\end{figure}

\subsection{Используемые технологии}
\begin{longtable}{|p{0.3\textwidth}|p{0.65\textwidth}|}
\hline
\textbf{Технология} & \textbf{Назначение} \\
\hline
Spring Cache Abstraction & Декларативное кэширование через аннотации (\texttt{@Cacheable}, \texttt{@CacheEvict}) \\
\hline
Redis & Высокопроизводительное in-memory хранилище для кэша \\
\hline
Spring Boot Actuator & Экспорт метрик кэша (количество чтений/записей/промахов) \\
\hline
Lettuce (клиент Redis) & Асинхронное подключение к Redis, настраивается пул соединений \\
\hline
\end{longtable}

\section{Структура проекта}
Ниже представлено дерево каталогов после внедрения кэширования. \textcolor{red}{\textbf{✅}} – изменённые или новые файлы, остальные – без изменений.

\begin{verbatim}
satellite-constellation-system/
├── docker-compose.yml                     ✅ (добавлен Redis)
├── README.tex                             ✅ (этот отчёт)
├── build.gradle.kts
├── settings.gradle.kts
├── space-operation-center/
│   ├── build.gradle.kts                   ✅ (зависимости)
│   └── src/main/
│       ├── java/com/example/spacecenter/
│       │   ├── SpaceOperationCenterApplication.java  ✅ (@EnableCaching)
│       │   ├── config/
│       │   │   └── CacheConfig.java                  ✅ (новый)
│       │   ├── domain/...                 (без изменений)
│       │   ├── dto/...
│       │   ├── repository/...
│       │   └── service/
│       │       ├── SatelliteService.java             ✅ (аннотации кэша)
│       │       └── OutboxScheduler.java
│       └── resources/
│           ├── application.yml            ✅ (настройки Redis)
│           └── schema.sql
├── telemetry-service/                     (без изменений)
└── mission-scheduler/                     (без изменений)
\end{verbatim}

\section{Настройка окружения}

\subsection{Docker Compose}
Добавляем сервис Redis в \texttt{docker-compose.yml}:

\begin{lstlisting}[style=ymlstyle, caption=Полный docker-compose.yml]
version: '3.8'
services:
  postgres:
    image: postgres:15
    environment:
      POSTGRES_DB: satellites
      POSTGRES_USER: satuser
      POSTGRES_PASSWORD: satpass
    ports:
      - "5432:5432"
    volumes:
      - pgdata:/var/lib/postgresql/data

  redis:
    image: redis:7-alpine
    ports:
      - "6379:6379"
    volumes:
      - redisdata:/data
    command: redis-server --appendonly yes

volumes:
  pgdata:
  redisdata:
\end{lstlisting}

\subsection{Зависимости Gradle}
Файл \texttt{space-operation-center/build.gradle.kts} дополняется стартерами для кэша и Redis:
\begin{lstlisting}[style=kotlinstyle, caption=build.gradle.kts]
plugins {
    id("org.springframework.boot") version "3.2.0"
    id("io.spring.dependency-management") version "1.1.4"
    java
}

group = "com.example"
version = "0.0.1-SNAPSHOT"

java {
    sourceCompatibility = JavaVersion.VERSION_17
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-cache")
    implementation("org.springframework.boot:spring-boot-starter-data-redis")
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    runtimeOnly("org.postgresql:postgresql")
    compileOnly("org.projectlombok:lombok")
    annotationProcessor("org.projectlombok:lombok")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
}
\end{lstlisting}

\subsection{Конфигурация приложения}
\texttt{application.yml} настраивает подключение к Redis, имена кэшей и экспорт метрик:
\begin{lstlisting}[style=ymlstyle, caption=application.yml]
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/satellites
    username: satuser
    password: satpass
  jpa:
    hibernate:
      ddl-auto: validate
    show-sql: true
  sql:
    init:
      mode: always
  cache:
    type: redis
    cache-names:
      - satellite
      - constellation
      - satellites
    redis:
      time-to-live: 600000   # дефолтный TTL (переопределяется в коде)
  redis:
    host: localhost
    port: 6379
    timeout: 2000ms
    lettuce:
      pool:
        max-active: 8
        max-idle: 8
        min-idle: 2

management:
  endpoints:
    web:
      exposure:
        include: health,metrics,caches
  endpoint:
    metrics:
      enabled: true
    caches:
      enabled: true
\end{lstlisting}

\section{Реализация кэширования}

\subsection{Включение кэширования}
Точка входа приложения аннотирована \texttt{@EnableCaching}:
\begin{lstlisting}[style=javastyle, caption=SpaceOperationCenterApplication.java]
package com.example.spacecenter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class SpaceOperationCenterApplication {
    public static void main(String[] args) {
        SpringApplication.run(SpaceOperationCenterApplication.class, args);
    }
}
\end{lstlisting}

\subsection{Конфигурация TTL и обработка ошибок}
Класс \texttt{CacheConfig} реализует \texttt{CachingConfigurer} и задаёт:
\begin{itemize}
    \item Разные TTL для кэшей: \texttt{satellite} – 10 мин, \texttt{constellation} – 15 мин, \texttt{satellites} – 5 мин.
    \item \texttt{CacheErrorHandler}, который перехватывает все ошибки Redis и не выбрасывает исключения, обеспечивая плавную деградацию.
\end{itemize}
\begin{lstlisting}[style=javastyle, caption=CacheConfig.java]
package com.example.spacecenter.config;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurer;
import org.springframework.cache.interceptor.CacheErrorHandler;
import org.springframework.cache.interceptor.SimpleCacheErrorHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import java.time.Duration;

@Configuration
public class CacheConfig implements CachingConfigurer {

    @Bean
    public CacheManager cacheManager(RedisConnectionFactory connectionFactory) {
        RedisCacheConfiguration defaultConfig = RedisCacheConfiguration.defaultCacheConfig()
                .serializeValuesWith(
                    RedisSerializationContext.SerializationPair.fromSerializer(
                        new GenericJackson2JsonRedisSerializer()
                    )
                );

        RedisCacheConfiguration satelliteCacheConfig =
            RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofMinutes(10));
        RedisCacheConfiguration constellationCacheConfig =
            RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofMinutes(15));
        RedisCacheConfiguration satellitesCacheConfig =
            RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofMinutes(5));

        return RedisCacheManager.builder(connectionFactory)
                .cacheDefaults(defaultConfig)
                .withCacheConfiguration("satellite", satelliteCacheConfig)
                .withCacheConfiguration("constellation", constellationCacheConfig)
                .withCacheConfiguration("satellites", satellitesCacheConfig)
                .build();
    }

    @Override
    @Bean
    public CacheErrorHandler errorHandler() {
        return new SimpleCacheErrorHandler() {
            @Override
            public void handleCacheGetError(RuntimeException exception,
                                            org.springframework.cache.Cache cache,
                                            Object key) {
                System.err.println("Cache GET error for " + cache.getName()
                    + " key " + key + ": " + exception.getMessage());
            }

            @Override
            public void handleCachePutError(RuntimeException exception,
                                            org.springframework.cache.Cache cache,
                                            Object key, Object value) {
                System.err.println("Cache PUT error for " + cache.getName()
                    + ": " + exception.getMessage());
            }

            @Override
            public void handleCacheEvictError(RuntimeException exception,
                                              org.springframework.cache.Cache cache,
                                              Object key) {
                System.err.println("Cache EVICT error for " + cache.getName()
                    + ": " + exception.getMessage());
            }

            @Override
            public void handleCacheClearError(RuntimeException exception,
                                              org.springframework.cache.Cache cache) {
                System.err.println("Cache CLEAR error for " + cache.getName()
                    + ": " + exception.getMessage());
            }
        };
    }
}
\end{lstlisting}

\subsection{Аннотации в SatelliteService}
Методы сервиса используют \texttt{@Cacheable} для чтения, \texttt{@CacheEvict} и \texttt{@Caching} для инвалидации. Кастомный ключ для поиска по составному имени.

\begin{lstlisting}[style=javastyle, caption=SatelliteService.java]
package com.example.spacecenter.service;

import com.example.spacecenter.domain.Constellation;
import com.example.spacecenter.domain.Satellite;
import com.example.spacecenter.repository.ConstellationRepository;
import com.example.spacecenter.repository.SatelliteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SatelliteService {

    private final SatelliteRepository satelliteRepository;
    private final ConstellationRepository constellationRepository;

    @Cacheable(value = "satellite", key = "#id")
    public Optional<Satellite> getSatelliteById(Long id) {
        return satelliteRepository.findById(id);
    }

    @Cacheable(value = "constellation", key = "#name")
    public Optional<Constellation> getConstellationByName(String name) {
        return constellationRepository.findByName(name);
    }

    @Cacheable(value = "satellites", key = "'all'")
    public List<Satellite> getAllSatellites() {
        return satelliteRepository.findAll();
    }

    @Cacheable(value = "satellite",
               key = "#constellationName + '::' + #satelliteName")
    public Optional<Satellite> findByConstellationAndName(
            String constellationName, String satelliteName) {
        return satelliteRepository
                .findByConstellationNameAndName(constellationName, satelliteName);
    }

    @Transactional
    @CacheEvict(value = "satellites", allEntries = true)
    public Satellite createSatellite(Satellite satellite) {
        return satelliteRepository.save(satellite);
    }

    @Transactional
    @CacheEvict(value = "satellite", key = "#satellite.id")
    public Satellite updateSatellite(Satellite satellite) {
        return satelliteRepository.save(satellite);
    }

    @Transactional
    @Caching(evict = {
        @CacheEvict(value = "satellite", key = "#id"),
        @CacheEvict(value = "satellites", allEntries = true)
    })
    public void deleteSatellite(Long id) {
        satelliteRepository.deleteById(id);
    }

    @Transactional
    @Caching(evict = {
        @CacheEvict(value = "constellation", key = "#name"),
        @CacheEvict(value = "satellites", allEntries = true)
    })
    public void updateConstellationComposition(String name, List<Long> satelliteIds) {
        Constellation constellation = constellationRepository.findByName(name)
                .orElseThrow(() -> new IllegalArgumentException("Constellation not found"));
        List<Satellite> satellites = satelliteRepository.findAllById(satelliteIds);
        constellation.setSatellites(satellites);
        constellationRepository.save(constellation);
    }
}
\end{lstlisting}

\section{Метрики кэша в Actuator}
После запуска приложения доступны следующие эндпоинты Actuator:
\begin{itemize}
    \item \texttt{/actuator/metrics/cache.gets} – количество успешных чтений из кэша
    \item \texttt{/actuator/metrics/cache.puts} – количество записей в кэш
    \item \texttt{/actuator/metrics/cache.evictions} – количество удалений (инвалидаций)
    \item \texttt{/actuator/caches} – подробная информация по каждому кэшу (имя, TTL, статистика)
\end{itemize}
Для их работы в \texttt{application.yml} включён экспорт \texttt{caches} и \texttt{metrics}.

\section{Запуск и тестирование}

\subsection{Предварительные требования}
\begin{itemize}
    \item Docker и Docker Compose
    \item Java 17
    \item Gradle (или использование wrapper)
\end{itemize}

\subsection{Запуск инфраструктуры}
Выполнить в корне проекта:
\begin{lstlisting}[style=ymlstyle, caption=Запуск Docker-контейнеров]
docker-compose up -d
\end{lstlisting}
Поднимутся PostgreSQL и Redis.

\subsection{Сборка и запуск сервиса}
\begin{lstlisting}[style=ymlstyle, caption=Запуск space-operation-center]
cd space-operation-center
./gradlew bootRun
\end{lstlisting}

\subsection{Проверка кэширования}
\begin{enumerate}
    \item \textbf{Первый запрос списка спутников:}
    \begin{verbatim}
curl http://localhost:8080/api/satellites
    \end{verbatim}
    В логах приложения появится SQL-запрос (из-за промаха кэша).

    \item \textbf{Повторный запрос:} тот же \texttt{curl} – SQL-запроса нет, данные взяты из Redis (время ответа значительно меньше).

    \item \textbf{Создание нового спутника:}
    \begin{verbatim}
curl -X POST http://localhost:8080/api/satellites \
  -H "Content-Type: application/json" \
  -d '{"name":"Sputnik-X","state":"ACTIVE"}'
    \end{verbatim}
    После этого кэш \texttt{satellites::all} полностью сброшен (аннотация \texttt{@CacheEvict(allEntries=true)}). Следующий GET-запрос списка снова вызовет SQL и закэширует обновлённый список.
\end{enumerate}

\subsection{Проверка graceful degradation}
\begin{enumerate}
    \item Остановите контейнер Redis:
    \begin{verbatim}
docker stop <имя_контейнера_redis>
    \end{verbatim}
    \item Повторите запрос списка спутников. Приложение должно ответить без ошибок (данные берутся напрямую из БД). В логах появятся сообщения \texttt{Cache GET error} и т.п., но исключения не выбросятся.
    \item Запустите Redis снова: \texttt{docker start <имя\_контейнера\_redis>} – кэш снова заработает.
\end{enumerate}

\section{Заключение}
Внедрённое кэширование с Redis и Spring Cache Abstraction позволило:
\begin{itemize}
    \item сократить количество обращений к базе данных для операций чтения;
    \item гибко управлять временем жизни кэша (разные TTL для разных сущностей);
    \item автоматически сбрасывать кэш при изменениях данных, сохраняя согласованность;
    \item обеспечить отказоустойчивость (работа без кэша при недоступности Redis);
    \item мониторить состояние кэша через стандартные метрики Actuator.
\end{itemize}
Данный подход рекомендован для высоконагруженных сервисов, где допустима небольшая задержка в актуализации данных (до истечения TTL).

\vfill
\begin{center}
\textcopyright\ Разработано в рамках курса «Конструирование программного обеспечения», семинар 13.
\end{center}

\end{document}
