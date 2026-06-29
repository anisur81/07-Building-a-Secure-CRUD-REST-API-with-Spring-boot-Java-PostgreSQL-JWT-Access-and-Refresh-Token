# CRUD REST API with Spring Boot, Java, PostgreSQL, JWT Access Token & Refresh Token Authentication

This guide demonstrates how to build a secure CRUD REST API using:

- **Java 21**
- **Spring Boot 3**
- **Spring Security 6**
- **PostgreSQL**
- **JWT Authentication**
- **Access Token**
- **Refresh Token**
- **Username & Password Login**
- **JPA/Hibernate**
- **Maven**

---

## Table of Contents

- [Phase 1 – Environment Setup](#phase-1--environment-setup)
- [Project Structure](#project-structure)
- [Step 1: Create PostgreSQL Database](#step-1-create-postgresql-database)
- [Step 2: Create Spring Boot Project](#step-2-create-spring-boot-project)
- [Step 3: Configure Database](#step-3-configure-database)
- [Step 4: Create the Required Code](#step-4-create-the-required-code)
- [API Testing](#api-testing)
  - [User Management](#user-management)
  - [Authentication](#authentication)
  - [Protected Resources](#protected-resources)
- [Security Configuration](#security-configuration)
- [Token Management](#token-management)

---

## Phase 1 – Environment Setup

### Step 1: Install Java JDK (17 or 21)

Verify installation:

```bash
java -version
javac -version
```

### Step 2: Install Maven

Verify installation:

```bash
mvn -version
```

### Step 3: Install PostgreSQL

Verify installation:

```bash
psql --version
```

Create a database:

```sql
CREATE DATABASE mydb;
```

### Step 4: Install an IDE

Recommended:

- **IntelliJ IDEA Community Edition**
- **Eclipse IDE**
- **Visual Studio Code** (with Java extensions)

### Step 5: Install Postman

Used for testing REST APIs.

---

## Project Structure

```
javarestapi
│
├── pom.xml
├── src
│   └── main
│       ├── java
│       │   └── com
│       │       └── example
│       │           └── restapi
│       │               ├── config
│       │               │   └── SecurityConfig.java
│       │               ├── controller
│       │               │   ├── AuthController.java
│       │               │   ├── ProductController.java
│       │               │   ├── ServiceTestController.java
│       │               │   └── UserController.java
│       │               ├── dto
│       │               │   ├── AuthResponse.java
│       │               │   ├── LoginRequest.java
│       │               │   └── RegisterRequest.java
│       │               ├── entity
│       │               │   ├── Product.java
│       │               │   ├── RefreshToken.java
│       │               │   └── User.java
│       │               ├── repository
│       │               │   ├── ProductRepository.java
│       │               │   ├── RefreshTokenRepository.java
│       │               │   └── UserRepository.java
│       │               ├── RestApiApplication.java
│       │               ├── security
│       │               │   ├── JwtAuthenticationFilter.java
│       │               │   └── JwtService.java
│       │               └── service
│       │                   ├── AuthService.java
│       │                   ├── CustomUserDetailsService.java
│       │                   ├── RefreshTokenService.java
│       │                   └── UserService.java
│       └── resources
│           └── application.properties
```

---

## Step 1: Create PostgreSQL Database

```sql
CREATE DATABASE mydb;
```

---

## Step 2: Create Spring Boot Project

### Dependencies: `pom.xml`

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0
                             https://maven.apache.org/xsd/maven-4.0.0.xsd">

    <modelVersion>4.0.0</modelVersion>

    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>3.5.3</version>
        <relativePath/>
    </parent>

    <groupId>com.example</groupId>
    <artifactId>javarestapi</artifactId>
    <version>0.0.1-SNAPSHOT</version>
    <name>javarestapi</name>
    <description>Spring Boot REST API with JWT</description>

    <properties>
        <java.version>21</java.version>
    </properties>

    <dependencies>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>

        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-security</artifactId>
        </dependency>

        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-data-jpa</artifactId>
        </dependency>

        <dependency>
            <groupId>org.postgresql</groupId>
            <artifactId>postgresql</artifactId>
            <scope>runtime</scope>
        </dependency>

        <dependency>
            <groupId>io.jsonwebtoken</groupId>
            <artifactId>jjwt-api</artifactId>
            <version>0.12.5</version>
        </dependency>

        <dependency>
            <groupId>io.jsonwebtoken</groupId>
            <artifactId>jjwt-impl</artifactId>
            <version>0.12.5</version>
            <scope>runtime</scope>
        </dependency>

        <dependency>
            <groupId>io.jsonwebtoken</groupId>
            <artifactId>jjwt-jackson</artifactId>
            <version>0.12.5</version>
            <scope>runtime</scope>
        </dependency>

        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-validation</artifactId>
        </dependency>

        <dependency>
            <groupId>org.projectlombok</groupId>
            <artifactId>lombok</artifactId>
            <optional>true</optional>
        </dependency>

        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
        </dependency>
    </dependencies>

    <build>
        <plugins>
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
            </plugin>
        </plugins>
    </build>
</project>
```

---

## Step 3: Configure Database

### `application.properties`

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/mydb
spring.datasource.username=postgres
spring.datasource.password=postgres

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true

jwt.secret=8a7f9c5d1e2b3a4f5c6d7e8f9a0b1c2d3e4f5a6b7c8d9e0f1a2b3c4d5e6f7a8
jwt.access.expiration=90000000
jwt.refresh.expiration=604800000
logging.level.org.springframework.security=TRACE
logging.level.org.springframework.web=DEBUG
```

### Token Configuration:
- **Access Token** = 15 minutes (900000 ms)
- **Refresh Token** = 7 days (604800000 ms)

---

## Step 4: Create the Required Code

 As per the code from repository

#### **Main Application Class**

<details>
<summary>RestApiApplication.java</summary>

```java
package com.example.restapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class RestApiApplication {
    public static void main(String[] args) {
        SpringApplication.run(RestApiApplication.class, args);
    }
}
```
</details>

---

## API Testing

### User Management (Without Authentication)

#### Create User

```bash
curl -X POST http://localhost:8080/api/users \
-H "Content-Type: application/json" \
-d '{
  "username":"john",
  "password":"123456",
  "role":"ROLE_USER"
}'
```

#### Get Single User

```bash
curl -i http://localhost:8080/api/users/1
```

#### Get All Users

```bash
curl -i http://localhost:8080/api/users
```

#### Update User

```bash
curl -X PUT http://localhost:8080/api/users/1 \
-H "Content-Type: application/json" \
-d '{
  "username":"john_updated",
  "password":"654321",
  "role":"ROLE_ADMIN"
}'
```

#### Delete User

```bash
curl -X DELETE -i http://localhost:8080/api/users/1
```

---

### Authentication

#### Register a New User

```bash
curl -X POST http://localhost:8080/api/auth/register \
-H "Content-Type: application/json" \
-d '{
  "username": "john2",
  "password": "password123",
  "role": "ROLE_USER"
}'
```

**Response:**
```json
{
  "id": 3,
  "username": "john2",
  "password": "$2a$10$TGW9Tld0Bl5P5qae1Dor0uI4gwo02F4vrRvCiyye2FJpanKLKs2Vu",
  "role": "ROLE_USER"
}
```

✅ **This means:**
- User was saved to PostgreSQL
- Password was encrypted using BCrypt
- ROLE_USER was assigned
- AuthService.register() is working

#### Login to Generate Tokens

```bash
response=$(curl -s -X POST http://localhost:8080/api/auth/login \
-H "Content-Type: application/json" \
-d '{
  "username":"john2",
  "password":"password123"
}')

echo "$response"
```

```bash
# Extract tokens
access_token=$(echo "$response" | jq -r '.accessToken')
refresh_token=$(echo "$response" | jq -r '.refreshToken')

echo "$access_token"
echo "$refresh_token"
```

#### Generate New Access Token Using Refresh Token

```bash
curl -X POST "http://localhost:8080/api/auth/refresh?refreshToken=fd7175a0-dbad-4464-b051-997786d9af80"
```

**Response:**
```json
{
  "accessToken": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJqb2huMiIsImlhdCI6MTc4MjYzMTIwMywiZXhwIjoxNzgyNjMyMTAzfQ.uL5VB1MvlLt30o3W3qU1hmMOlfovHVF6LHdcmtjVQ3g",
  "refreshToken": "fd7175a0-dbad-4464-b051-997786d9af80"
}
```

---

### Protected Resources

#### Get Single User (Protected Endpoint)

```bash
curl http://localhost:8080/api/users/1 \
-H "Authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJqb2huMiIsImlhdCI6MTc4MjcxNjU4OSwiZXhwIjoxNzgyNzE3NDg5fQ.WJRqaHiRAD8zE6-TjiPmyu7QZtto3Pn_GCMtD_5BnP0"
```

**Response:**
```json
{
  "id": 1,
  "username": "john_updated",
  "password": "654321",
  "role": "ROLE_ADMIN"
}
```

#### Product CRUD Operations

##### Create Product

```bash
curl -X POST http://localhost:8080/api/products \
-H "Authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJqb2huMiIsImlhdCI6MTc4MjcxNjU4OSwiZXhwIjoxNzgyNzE3NDg5fQ.WJRqaHiRAD8zE6-TjiPmyu7QZtto3Pn_GCMtD_5BnP0" \
-H "Content-Type: application/json" \
-d '{
  "name": "Laptop xyz",
  "price": 1900
}'
```

**Response:**
```json
{
  "id": 1,
  "name": "Laptop",
  "price": 1200.0
}
```

##### Get All Products

```bash
curl http://localhost:8080/api/products \
-H "Authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJqb2huMiIsImlhdCI6MTc4MjcxNjU4OSwiZXhwIjoxNzgyNzE3NDg5fQ.WJRqaHiRAD8zE6-TjiPmyu7QZtto3Pn_GCMtD_5BnP0"
```

**Response:**
```json
{
  "id": 1,
  "name": "Laptop",
  "price": 1200.0
}
```

##### Get Single Product

```bash
curl http://localhost:8080/api/products/1 \
-H "Authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJqb2huMiIsImlhdCI6MTc4MjcxNjU4OSwiZXhwIjoxNzgyNzE3NDg5fQ.WJRqaHiRAD8zE6-TjiPmyu7QZtto3Pn_GCMtD_5BnP0"
```

**Response:**
```json
{
  "id": 1,
  "name": "Laptop",
  "price": 1200.0
}
```

##### Update Product

```bash
curl -X PUT http://localhost:8080/api/products/1 \
-H "Authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJqb2huMiIsImlhdCI6MTc4MjYzNzg1OCwiZXhwIjoxNzgyNjM4NzU4fQ.GlLWujZ3WF7qeBFsurPfanNTKyg0AxFhl4IYoPU3XkU" \
-H "Content-Type: application/json" \
-d '{
  "name": "Gaming Laptop",
  "price": 1500
}'
```

**Response:**
```json
{
  "id": 1,
  "name": "Gaming Laptop",
  "price": 1500.0
}
```

##### Delete Product

```bash
curl -X DELETE http://localhost:8080/api/products/2 \
-H "Authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJqb2huMiIsImlhdCI6MTc4MjYzNzg1OCwiZXhwIjoxNzgyNjM4NzU4fQ.GlLWujZ3WF7qeBFsurPfanNTKyg0AxFhl4IYoPU3XkU"
```

#### Employee Endpoints (ServiceTestController)

##### Create Employee

```bash
curl -X POST http://localhost:8080/api/employees \
-H "Authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJqb2huMiIsImlhdCI6
