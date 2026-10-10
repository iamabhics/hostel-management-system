# Hostel Management – Backend

## About

The backend of the Hostel Management – Verified Reviews and Ratings application. It provides student authentication, hostel data, and review management APIs.

## Technologies

* Java 21
* Spring Boot
* Spring Security and JWT
* Spring Data JPA / Hibernate
* MySQL
* Maven

## Requirements

* Java 21
* Maven
* MySQL Server

## Database

Create a MySQL database named `hostel_management`.

Configure these environment variables before starting the backend:

* `DB_URL`
* `DB_USERNAME`
* `DB_PASSWORD`
* `JWT_SECRET`

Never publish actual database passwords or JWT secrets.

## Run the Backend

From this folder, run:

```bash
mvn spring-boot:run
```

Backend address: `http://localhost:8080`

## Project Status

Under development. Basic login, hostel listing, and review display have been tested. Review authorization and ownership checks are still being tested.
