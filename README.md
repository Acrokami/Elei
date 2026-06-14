
Elei is a high-load gamification platform for productivity. It transforms routine tasks, learning, and training into a structured game loop — complete with an XP system, dynamic leveling, an activity heatmap, and a fully transactional internal economy.


⚙️ Engineering Highlights

Designed with resilience, clean architecture, and enterprise-grade patterns in mind:


Domain-Driven Design (DDD): Business logic is strictly separated into independent domains — user, economy, experience, and quest.
ACID & Concurrency Control: The System Market is protected against race conditions using pessimistic database locks (@Lock(LockModeType.PESSIMISTIC_WRITE)).
Event-Driven Architecture: Spring ApplicationEvents decouple modules — for example, wallet initialization is triggered asynchronously on citizen registration.
Sliced Testing: Data-layer integration tests run against an isolated PostgreSQL instance spun up via Testcontainers, validating concurrent transaction correctness.
CI/CD Pipeline: GitHub Actions pipeline with automated builds, dependency caching (Node 24 / JDK 21), and DockerHub image publishing.



🚀 Core Modules

#ModuleDescription1Identity & SecurityJWT auth, password encryption, role-based access (USER / ADMIN)2Progression SystemAlgorithmic XP-based level and rank calculation3Activity HeatmapGlobal productivity matrix, GitHub Contributions–style4System MarketInternal reward shop with strict transactional stock and balance control5Daily Protocols(WIP) Daily check-ins and quest system


🛠️ Tech Stack

Backend

LayerTechnologyCoreJava 21, Spring Boot 3.xSecuritySpring Security, JWTDataSpring Data JPA, Hibernate, PostgreSQLMigrationsFlywayTestingJUnit 5, Testcontainers

Frontend

LayerTechnologyCoreVue 3 (Composition API), TypeScriptRouting & StateVue RouterNetworkAxios with token interceptorsUI/UXCustom Glassmorphism Cyber-design, CSS Variables

Infrastructure


Docker & Docker Compose
GitHub Actions (CI/CD)



🏁 Local Deployment

Prerequisites


Java 21 (JDK)
Node.js 24+
Docker & Docker Compose
Maven


Step 1 — Start Infrastructure

Spin up the PostgreSQL database with Docker Compose:

bashdocker-compose up -d

Step 2 — Start Backend

Navigate to the backend directory and run the app. Flyway will automatically apply all migrations and seed the market with starter items.

bashcd backend
mvn spring-boot:run


Backend will be available at http://localhost:8080



Step 3 — Start Frontend

Open a new terminal, navigate to frontend, install dependencies, and start the dev server:

bashcd frontend
npm install
npm run dev


Frontend will be available at http://localhost:5173


🧪 Testing

Race condition protection in the economy module is covered by stress tests using Testcontainers:

bashcd backend
mvn test


Note: Stress tests are currently a work in progress.



# Elei: System Protocol 💠

![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.2-6DB33F?style=for-the-badge&logo=springboot)
![Vue.js](https://img.shields.io/badge/Vue.js-3.0-4FC08D?style=for-the-badge&logo=vue.js)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-15-336791?style=for-the-badge&logo=postgresql)
![Java](https://img.shields.io/badge/Java-21-ED8B00?style=for-the-badge&logo=openjdk)
![Docker](https://img.shields.io/badge/Docker-Ready-2496ED?style=for-the-badge&logo=docker)

**Elei** — это высоконагруженная платформа геймификации продуктивности. Проект трансформирует рутинные задачи, обучение и тренировки в структурированный игровой процесс с системой опыта (XP), динамическими уровнями, картой активности и собственной безопасной экономикой.

---

## ⚙️ Архитектурные особенности (Engineering Highlights)

Проект спроектирован с упором на отказоустойчивость, чистую архитектуру и Enterprise-паттерны:

* **Domain-Driven Design (DDD):** Строгое разделение бизнес-логики на независимые домены (`user`, `economy`, `experience`, `quest`).
* **ACID & Concurrency Control:** Ядро системы экономики (System Market) защищено от *Race Conditions* с использованием пессимистических блокировок БД (`@Lock(LockModeType.PESSIMISTIC_WRITE)`).
* **Event-Driven Architecture:** Использование `Spring ApplicationEvents` для слабой связанности модулей (например, асинхронная инициализация кошелька при регистрации гражданина).
* **Срезовое тестирование (Sliced Testing):** Интеграционные тесты слоя данных и транзакций с использованием **Testcontainers** (поднятие изолированного PostgreSQL в Docker для проверки многопоточности).
* **CI/CD Pipeline:** Настроенный конвейер GitHub Actions с автоматической сборкой, кэшированием зависимостей (Node 24 / JDK 21) и публикацией образов в DockerHub.

---

## 🚀 Основные модули (Core Protocols)

1. **Identity & Security:** JWT-авторизация, шифрование паролей, разделение ролей (USER/ADMIN).
2. **Progression System:** Алгоритмический расчет уровней и рангов на основе накопленного опыта.
3. **Activity Heatmap:** Глобальная матрица продуктивности (в стиле GitHub Contributions).
4. **System Market:** Внутренний магазин наград со строгим транзакционным контролем стока и баланса.
5. **Daily Protocols:** (В разработке) Ежедневные чекины и система квестов.

---

## 🛠️ Технологический стек (Tech Stack)

### Backend
* **Core:** Java 21, Spring Boot 3.x
* **Security:** Spring Security, JWT (JSON Web Tokens)
* **Data:** Spring Data JPA, Hibernate, PostgreSQL
* **Migrations:** Flyway
* **Testing:** JUnit 5, Testcontainers

### Frontend
* **Core:** Vue 3 (Composition API), TypeScript
* **Routing & State:** Vue Router
* **Network:** Axios (с интерцепторами для обработки токенов)
* **UI/UX:** Custom "Glassmorphism" Cyber-design, CSS Variables for theming

### Infrastructure
* Docker & Docker Compose
* GitHub Actions (CI/CD Pipeline)

---

## 🏁 Развертывание локально (Local Deployment)

### Требования (Prerequisites)
* Java 21 (JDK)
* Node.js 24+
* Docker & Docker Compose
* Maven

### Шаг 1: Запуск инфраструктуры
Поднимите базу данных PostgreSQL с помощью Docker Compose:
```
docker-compose up -d
```

Шаг 2: Запуск Backend (Spring Boot)
Перейдите в директорию backend и запустите приложение. Flyway автоматически накатит все необходимые миграции и наполнит базу данных стартовыми товарами для магазина.

```
cd backend
mvn spring-boot:run
```
Backend будет доступен на порту 8080.

Шаг 3: Запуск Frontend (Vue.js)
Откройте новый терминал, перейдите в директорию frontend и установите зависимости:

```
cd frontend
npm install
npm run dev
```
Frontend будет доступен по адресу http://localhost:5173.

🧪 Тестирование (Testing) 
Для проверки защиты от состояния гонки (Race Condition) в экономическом модуле реализованы стресс-тесты с использованием Testcontainers:
P.S WIP
```
cd backend
mvn test
```
