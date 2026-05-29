# Driverio Backend 🚗💼

Welcome to **Driverio Backend**, a robust and secure corporate logistics and cab dispatch control system built using **Spring Boot 3**. This system enables enterprise organizations to coordinate vehicle scheduling, automate driver assignments, and securely manage role-based portals for Admins, HR Specialists, IT Staff, and Drivers.

---
## Live Demo
https://driverio-backend-1.onrender.com

## 🌟 Key Features

*   **Role-Based Access Control (RBAC)**: Custom routing and authorization paths mapped specifically for `ADMIN`, `HR`, `IT`, and `DRIVER` roles using JSON Web Tokens (JWT).
*   **Direct & Streamlined Authentication**: Safe direct credentials login using `BCryptPasswordEncoder` that issues structured secure tokens immediately upon verification.
*   **Strict Email Syntax & Domain Validation**: Enforces robust regular expression formatting and automatically blacklists known disposable/throwaway email providers (e.g. `yopmail.com`, `mailinator.com`) during registration.
*   **Isolated Data Workspaces**: Custom JPA Repository query methods enforce account boundaries—ensuring HR and IT staff start with empty workspaces and only see their own transport logs and stats.
*   **Automated Driver & OTP Dispatch**: Schedules bookings instantly, automatically matching passenger requests to available cab classifications (Sedans vs. SUVs) and generating secure 4-digit verification OTPs.
*   **Resilient SMTP Notification Framework**: Integrated mail notification service wrapped in resilient error handling. If the SMTP provider is unresponsive or unauthenticated, core dispatch transactions succeed without interruption.

---

## 🛠️ Technology Stack

*   **Core Framework**: Spring Boot 3.4.7 (Java 17+)
*   **Security & Auth**: Spring Security, JWT (JSON Web Tokens), BCrypt Cryptography
*   **Database & ORM**: MySQL Database, Spring Data JPA, Hibernate ORM
*   **Notification Integration**: JavaMailSender, Twilio SMS SDK
*   **AI Integration**: HuggingFace Inference API (supporting FLAN-T5 Models)
*   **Build Tool**: Apache Maven (includes Maven Wrapper `mvnw`)

---

## 📋 API Architecture & Endpoints

### 🔐 Authentication (`/api/auth`)

| Method | Endpoint | Description | Access |
| :--- | :--- | :--- | :--- |
| **POST** | `/api/auth/register` | Registers a new user (with strict email and throwaway domain checks) | Public |
| **POST** | `/api/auth/login` | Validates credentials and returns a JWT security token and role details | Public |

### 🚕 HR/IT Services (`/api/hr`)

| Method | Endpoint | Description | Access |
| :--- | :--- | :--- | :--- |
| **POST** | `/api/hr/book` | Schedules a new cab booking and assigns available drivers | HR/IT |
| **GET** | `/api/hr/all-bookings` | Retrieves bookings. Accepts optional `?email=...` query to isolate logs | HR/IT |

### 📋 Admin Management (`/api/admin`)

| Method | Endpoint | Description | Access |
| :--- | :--- | :--- | :--- |
| **GET** | `/api/admin/all-bookings` | Monitors and lists all booking dispatches across the enterprise | Admin |

### 🚗 Driver Operations (`/api/driver`)

| Method | Endpoint | Description | Access |
| :--- | :--- | :--- | :--- |
| **GET** | `/api/driver/trips` | Retrieves active trips and bookings matching the driver's email | Driver |

---

## 🚀 Getting Started

### Prerequisites

*   **Java**: JDK 17 or higher installed on your system.
*   **MySQL**: Running local instance of MySQL database server.

### Configuration

Open [src/main/resources/application.properties](src/main/resources/application.properties) and update your database and notification credentials:

```properties
# Database Configuration
spring.datasource.url=jdbc:mysql://localhost:3306/rolebased_login?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC
spring.datasource.username=YOUR_MYSQL_USERNAME
spring.datasource.password=YOUR_MYSQL_PASSWORD

# SMTP Configuration (Optional)
spring.mail.host=smtp.gmail.com
spring.mail.port=587
spring.mail.username=YOUR_GMAIL_ADDRESS
spring.mail.password=YOUR_GMAIL_APP_PASSWORD
```

### Running the Application

In your terminal, navigate to the project directory and execute:

```powershell
# On Windows (PowerShell)
.\mvnw.cmd spring-boot:run

# On Linux/macOS
./mvnw spring-boot:run
```

The application will start on **`http://localhost:8080`**.

---

## 🔒 Security Design & Robustness

1.  **Password Safety**: User credentials are encrypted at rest using salt-strengthened **BCrypt** hashing.
2.  **Stateless Sessions**: JWT tokens enable stateless API authorization, protecting endpoints against cross-site request forgery.
3.  **Resiliency**: External APIs (like HuggingFace, SMTP, Twilio) are separated from core business models, ensuring maximum uptime even if external systems encounter network failures.
