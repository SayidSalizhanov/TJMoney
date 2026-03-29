[![Review Assignment Due Date](https://classroom.github.com/assets/deadline-readme-button-22041afd0340ce965d47ae6ef1cefeee28c7c493a6346c4f15d667ab976d596c.svg)](https://classroom.github.com/a/0tljtIcS)

# TJMoney — Personal Finance Management System

TJMoney is a full-stack web application for personal and group financial management. It allows users to track transactions, set financial goals, collaborate in groups, chat in real-time, and receive ML-based predictions of future expenses.

---

## Features

- **Transaction tracking** — add, edit, delete income/expense transactions with categories; bulk import via CSV
- **Financial goals** — create and monitor progress toward personal or group goals
- **Group collaboration** — create groups, manage members, share transactions and goals within a group
- **Real-time chat** — WebSocket-based group chat with message history
- **ML expense prediction** — Random Forest model predicts next month's expenses by category
- **Currency rates** — live forex exchange rates via FX Rates API
- **Reminders** — scheduled reminders with email notifications
- **Educational articles** — finance articles published by admins
- **Avatar management** — profile picture upload via Cloudinary

---

## Architecture

The project is a multi-service application orchestrated via Docker Compose:

```
semester-work-spring-SayidSalizhanov/
├── rest-api/          # Spring Boot backend (port 8080)
│   ├── api/           # API interfaces and DTOs
│   ├── impl/          # Business logic, controllers, services
│   └── db/            # JPA entities
├── main-service/      # Spring Boot + Thymeleaf frontend BFF (port 3000)
├── ml-service/        # Django ML prediction service (port 8000)
├── jacoco/            # Code coverage aggregation
└── docker-compose.yml
```

---

## Technology Stack

### Backend (REST API)
| Component | Technology |
|-----------|-----------|
| Language | Java 21 |
| Framework | Spring Boot 3.4.4 |
| Security | Spring Security + JWT |
| ORM | Spring Data JPA / Hibernate |
| Database | PostgreSQL 15 |
| Migrations | Liquibase |
| Mapping | MapStruct |
| Image Hosting | Cloudinary |
| CSV Parsing | OpenCSV |
| API Docs | OpenAPI / Swagger |
| Real-time | WebSocket (STOMP) |
| Email | SMTP (Mail.ru) |
| Build | Gradle |
| Testing | JUnit 5, Testcontainers, JaCoCo |

### Frontend (Main Service)
| Component | Technology |
|-----------|-----------|
| Language | Java 21 |
| Framework | Spring Boot 3.4.4 |
| Templates | Thymeleaf |
| Styling | HTML5 / CSS3 / JavaScript |
| Real-time | WebSocket client |

### ML Service
| Component | Technology |
|-----------|-----------|
| Language | Python |
| Framework | Django |
| Model | Random Forest Regressor (scikit-learn) |
| Libraries | NumPy |
| Storage | SQLite |

---

## Domain Model

| Entity | Description |
|--------|-------------|
| `User` | Application user with roles, avatar, Telegram ID |
| `Group` | Financial collaboration group |
| `GroupMember` | User membership record with role (ADMIN / MEMBER) |
| `Transaction` | Income or expense record with category, amount, date |
| `Goal` | Financial goal with progress tracking |
| `Record` | Text note or document within a group |
| `Reminder` | Scheduled notification sent via email |
| `Article` | Educational finance article (admin-created) |
| `ChatMessageEntity` | Persisted real-time chat message |
| `Application` | Request to join a group (pending / accepted / rejected) |
| `Avatar` | Profile picture stored in Cloudinary |
| `Role` | User permission role (USER / ADMIN) |

### Key Relationships
- User ↔ Group: Many-to-Many via `GroupMember`
- User → Transactions, Goals, Records, Reminders: One-to-Many
- Group → Transactions, Goals, Records, Chat Messages: One-to-Many
- User → Avatar: One-to-One
- User ↔ Role: Many-to-Many

### Transaction Categories
`Food`, `Transport`, `Housing`, `Entertainment`, `Clothing`, `Health`, `Education`, `Other`

---

## API Overview

| Module | Base Path | Key Operations |
|--------|-----------|----------------|
| Auth | `/api/auth` | login, register, logout |
| User | `/api/user` | profile, settings, avatar, password, groups |
| Groups | `/api/groups` | CRUD, members, applications, join/leave |
| Transactions | `/api/transactions` | CRUD, CSV upload, ML prediction |
| Goals | `/api/goals` | CRUD |
| Records | `/api/records` | CRUD |
| Reminders | `/api/reminders` | CRUD |
| Articles | `/api/articles` | list (public), create (admin) |
| Chat | `/api/chat` | history, send, join/leave; WebSocket via STOMP |
| Currency | `/api/currency` | live exchange rates |

Full Swagger documentation is available at `/swagger-ui.html` when the REST API is running.

---

## Frontend Pages

| Section | Pages |
|---------|-------|
| Public | Home (articles), About, Login, Register, Currency rates |
| User | Profile, Settings, Password change, Avatar, Groups list, Applications |
| Groups | Browse groups, Create group, Group profile, Members, Admin panel, Chat |
| Transactions | List, Create, Edit, CSV upload, Expense prediction |
| Goals | List, Create, Edit |
| Records | List, Create, Edit |
| Reminders | List, Create, Edit |
| Admin | Create article |

---

## ML Expense Prediction

The ML service exposes a prediction endpoint consumed by the REST API at `/api/transactions/predict-expenses`.

- **Model**: Random Forest Regressor trained per user per category
- **Input**: historical transaction data grouped by month and category
- **Output**: predicted expense amounts for each category in the next month
- **Edge cases**: handles insufficient data gracefully; clips outliers at the 95th percentile

---

## Security

- JWT Bearer tokens with 24-hour expiration
- BCrypt password hashing
- Role-based access control: `USER` and `ADMIN`
- Stateless session management
- Public endpoints: `/api/auth/**`, `/api/articles` (read), `/api/currency/**`, WebSocket

---

## Running the Project

### Prerequisites
- Docker and Docker Compose installed

### Start all services

```bash
docker-compose up --build
```

| Service | URL |
|---------|-----|
| Frontend | http://localhost:3000 |
| REST API | http://localhost:8080 |
| ML Service | http://localhost:8000 |
| Swagger UI | http://localhost:8080/swagger-ui.html |

### Environment / Configuration

Key settings are in `rest-api/impl/src/main/resources/application.yml`:

```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/tj-money
jwt:
  expiration: 86400000   # 24 hours
cloudinary:
  # Cloudinary API credentials
fxRatesApi:
  # FX Rates API key
mail:
  # SMTP credentials
```

---

## Development

### Build REST API

```bash
cd rest-api
./gradlew build
```

### Run tests with coverage

```bash
./gradlew test jacocoTestReport
```

### Run ML service locally

```bash
cd ml-service
pip install -r requirements.txt
python manage.py runserver 8000
```

---

## Project Structure Details

```
rest-api/impl/src/main/java/ru/itis/impl/
├── config/          # Security, WebSocket, app config
├── controller/      # 11 REST controllers
├── service/         # Business logic
├── repository/      # Spring Data JPA repositories
├── model/           # 12 JPA entities (in db module)
├── dto/             # Request/Response DTOs (in api module)
├── mapper/          # MapStruct mappers
├── security/        # JWT filters and utilities
└── exception/       # Custom exception hierarchy

main-service/src/main/
├── java/.../controller/   # 16 MVC controllers
└── resources/
    ├── templates/          # 37 Thymeleaf templates
    └── static/css/         # Page-specific stylesheets
```
