# Hostel Management System – Verified Reviews and Ratings

A full-stack web application designed to help students explore hostel information and share reviews and ratings. The project combines a React frontend with a Java Spring Boot backend and a MySQL database.

## 📌 Project Overview

The Hostel Management System aims to make hostel information more accessible to students by providing hostel listings, hostel details, user registration and login, and a review and rating system for verified students.

The frontend communicates with the backend through REST APIs, while the backend handles application logic, authentication, and database operations.

## ✨ Features

- **Student Registration and Login:** Allows students to register and log in.
- **Hostel Listings:** Displays available hostel information.
- **Hostel Details:** Provides detailed information about individual hostels.
- **Verified Student Reviews:** Supports reviews from students with the appropriate verified role.
- **Ratings:** Supports hostel rating functionality.
- **REST API Integration:** Connects the React frontend with the Spring Boot backend.
- **Authentication:** Uses JWT-based authentication and password encoding.

## 🛠️ Technologies Used

### Frontend
- React
- JavaScript
- Vite
- HTML and CSS
- Axios
- React Router

### Backend
- Java
- Spring Boot
- Spring Security
- Spring Data JPA
- Maven
- JWT authentication

### Database
- MySQL

### Tools
- Visual Studio Code
- Git and GitHub
- Thunder Client for API testing

## 📂 Project Structure

```text
hostel-management-system/
├── README.md
├── .gitignore
├── hostel-frontend/
│   ├── public/
│   ├── src/
│   │   ├── pages/
│   │   ├── services/
│   │   ├── App.jsx
│   │   └── main.jsx
│   ├── package.json
│   └── vite.config.js
└── hostel-management/
    ├── src/
    │   ├── main/
    │   └── test/
    ├── pom.xml
    ├── mvnw
    └── mvnw.cmd
```

## 🚀 Getting Started

### Prerequisites

Install the following before running the project:Hostel Management System – Verified Reviews and Ratings

A full-stack web application designed to help students explore hostel information and share reviews and ratings. The project combines a React frontend with a Java Spring Boot backend and a MySQL database.

📌 Project Overview

The Hostel Management System aims to make hostel information more accessible to students by providing hostel listings, hostel details, user registration and login, and a review and rating system for verified students.

The frontend communicates with the backend through REST APIs, while the backend handles application logic, authentication, and database operations.

✨ Features

Student Registration and Login: Allows students to register and log in.

Hostel Listings: Displays available hostel information.

Hostel Details: Provides detailed information about individual hostels.

Verified Student Reviews: Supports reviews from students with the appropriate verified role.

Ratings: Supports hostel rating functionality.

REST API Integration: Connects the React frontend with the Spring Boot backend.

Authentication: Uses JWT-based authentication and password encoding.

🛠️ Technologies Used

Frontend

React

JavaScript

Vite

HTML and CSS

Axios

React Router

Backend

Java

Spring Boot

Spring Security

Spring Data JPA

Maven

JWT authentication

Database

MySQL

Tools

Visual Studio Code

Git and GitHub

Thunder Client for API testing

📂 Project Structure

hostel-management-system/
├── README.md
├── .gitignore
├── hostel-frontend/
│   ├── public/
│   ├── src/
│   │   ├── pages/
│   │   ├── services/
│   │   ├── App.jsx
│   │   └── main.jsx
│   ├── package.json
│   └── vite.config.js
└── hostel-management/
    ├── src/
    │   ├── main/
    │   └── test/
    ├── pom.xml
    ├── mvnw
    └── mvnw.cmd

🚀 Getting Started

Prerequisites

Install the following before running the project:

Java 21

Node.js and npm

MySQL Server

Git

1. Clone the Repository

git clone https://github.com/iamabhics/hostel-management-system.git
cd hostel-management-system

2. Configure the MySQL Database

Create a database in MySQL:

CREATE DATABASE hostel_management;

Configure the database connection using environment variables:

DB_URL — MySQL JDBC connection URL

DB_USERNAME — MySQL username

DB_PASSWORD — MySQL password

The default database URL in the backend configuration is:

jdbc:mysql://localhost:3306/hostel_management

3. Configure JWT

Set the following environment variable before starting the backend:

JWT_SECRET — a securely generated secret suitable for signing JWTs.

The backend also expects SAMPLE_OLD_STUDENT_PASSWORD if the sample student account initializer needs to create its test account.

Do not commit real passwords, database credentials, or JWT secrets to GitHub.

4. Run the Backend

Open a terminal in the backend directory:

cd hostel-management

On Windows PowerShell:

.\mvnw.cmd spring-boot:run

The backend is configured to use port 8080 by default.

5. Run the Frontend

Open another terminal in the frontend directory:

cd hostel-frontend
npm install
npm run dev

Open the local URL displayed by Vite in your terminal, commonly:

http://localhost:5173

Ensure the frontend API configuration points to the running backend.

🔐 Security Notes

Passwords should be stored using secure password hashing.

JWT secrets and database credentials must be supplied through environment variables.

Sample accounts are intended for development and testing only.

Production deployments should use secure configuration and HTTPS.

🌐 Deployment

The project source code is available on GitHub. Live deployment of the frontend and backend, along with hosted database configuration, is planned separately.

👨‍💻 Author

Abhishek Mukkera

GitHub: iamabhics

📄 License

No license has been specified yet.

- Java 21
- Node.js and npm
- MySQL Server
- Git

### 1. Clone the