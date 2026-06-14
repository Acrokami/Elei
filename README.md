
 Elei: System Protocol 💠

![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.2-6DB33F?style=for-the-badge&logo=springboot)
![Vue.js](https://img.shields.io/badge/Vue.js-3.0-4FC08D?style=for-the-badge&logo=vue.js)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-15-336791?style=for-the-badge&logo=postgresql)
![Java](https://img.shields.io/badge/Java-21-ED8B00?style=for-the-badge&logo=openjdk)
![Docker](https://img.shields.io/badge/Docker-Ready-2496ED?style=for-the-badge&logo=docker)

**Elei** is a high-load productivity gamification platform. The project transforms routine tasks, learning, and workouts into a structured gaming process with an experience (XP) system, dynamic leveling, activity heatmaps, and a secure internal economy.

---

## ⚙️ Engineering Highlights

The project is designed with a strong focus on fault tolerance, clean architecture, and Enterprise-level patterns:

* **Domain-Driven Design (DDD):** Strict separation of business logic into independent domains (`user`, `economy`, `experience`, `quest`).
* **ACID & Concurrency Control:** The System Market core is protected against *Race Conditions* using pessimistic database locks (`@Lock(LockModeType.PESSIMISTIC_WRITE)`).
* **Event-Driven Architecture:** Leveraging `Spring ApplicationEvents` for loose coupling of modules (e.g., asynchronous wallet initialization upon citizen registration).
* **Sliced Testing:** Integration testing of the data and transaction layers using **Testcontainers** (spinning up isolated PostgreSQL instances in Docker to verify multithreading behavior).
* **CI/CD Pipeline:** Fully configured GitHub Actions workflow with automated builds, dependency caching (Node 24 / JDK 21), and image publishing to DockerHub.

---

## 🚀 Core Protocols

1. **Identity & Security:** JWT authorization, password hashing, and role-based access control (USER/ADMIN).
2. **Progression System:** Algorithmic calculation of levels and ranks based on accumulated experience.
3. **Activity Heatmap:** Global productivity matrix (GitHub Contributions style).
4. **System Market:** Internal rewards store with strict transactional control of stock and user balances.
5. **Daily Protocols:** (In development) Daily check-ins and dynamic quest generation.

---

## 🛠️ Tech Stack

### Backend
* **Core:** Java 21, Spring Boot 3.x
* **Security:** Spring Security, JWT (JSON Web Tokens)
* **Data:** Spring Data JPA, Hibernate, PostgreSQL
* **Migrations:** Flyway
* **Testing:** JUnit 5, Testcontainers

### Frontend
* **Core:** Vue 3 (Composition API), TypeScript
* **Routing & State:** Vue Router
* **Network:** Axios (with interceptors for token handling)
* **UI/UX:** Custom "Glassmorphism" Cyber-design, CSS Variables for system theming

### Infrastructure
* Docker & Docker Compose
* GitHub Actions (CI/CD Pipeline)

---

## 🏁 Local Deployment

### Prerequisites
* Java 21 (JDK)
* Node.js 24+
* Docker & Docker Compose
* Maven

### Step 1: Initialize Infrastructure
Spin up the PostgreSQL database using Docker Compose:
```
docker-compose up -d
```


Step 2: Boot Backend
Navigate to the backend directory and run the application. Flyway will automatically apply all database migrations and populate the market with initial items.

```
cd backend
mvn spring-boot:run
```
The backend API will be available on port 8080.


Step 3: Boot Frontend
Open a new terminal, navigate to the frontend directory, install dependencies, and start the dev server:
```
cd frontend
npm install
npm run dev
```
The frontend interface will be available at http://localhost:5173.



🧪 Testing
To verify the robust protection against Race Conditions in the economy module, the project includes multithreaded stress tests utilizing Testcontainers:

```
cd backend
mvn test
```
This test executes 10 parallel threads attempting to purchase a single item simultaneously, proving the reliability of the PostgreSQL pessimistic locking architecture.



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
