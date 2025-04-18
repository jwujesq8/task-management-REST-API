# Task Management API

## Project Description

This is a Spring Boot-based REST API for managing tasks. It features JWT authentication, role-based access control, and integrates with a PostgreSQL database. The application allows admins and users to manage tasks, post comments, and authenticate via JWT tokens.

## Technologies Used

- **Java** (Spring Boot)
- **PostgreSQL** (Database)
- **Docker** (Containerization)
- **JWT** (Authentication)
- **Swagger/OpenAPI** (API Documentation)

## Prerequisites

- Docker and Docker Compose installed
- Java 22+ installed
- Maven or Gradle for building the project

## Setup Instructions

1. Clone the repository:
    ```bash
    git clone https://github.com/jwujesq8/task-management-REST-API.git
    cd <project_directory>
    ```

2. Set up Docker:
    - Ensure Docker and Docker Compose are installed on your system.

3. Build the project:
    ```bash
    ./mvnw clean install
    ```

4. Start the application using Docker Compose:
    ```bash
    docker-compose up --build
    ```

   This will set up the API container and PostgreSQL container as defined in the `docker-compose.yml`.

5. Visit the API at:
    - [http://localhost:8080](http://localhost:8080)

## Configuration

### Application Properties (`application.properties`)

- `spring.application.name=api`  
  Specifies the name of the Spring Boot application.

- **PostgreSQL Configuration**
    - `spring.datasource.url=jdbc:postgresql://localhost:5432/rest-api`
    - `spring.datasource.username=postgres`
    - `spring.datasource.password=12345`

- **JWT Configuration**
    - `jwt.access.path=api/src/main/resources/jwt/access.txt`
    - `jwt.refresh.path=api/src/main/resources/jwt/refresh.txt`

### Docker Compose Configuration (`docker-compose.yml`)

The `docker-compose.yml` file contains the services for the API and PostgreSQL. The API container is built from the Dockerfile and connects to a PostgreSQL database. The `depends_on` ensures that the database container is started before the API.

## Endpoints

### Auth Endpoints

- **POST** `/auth/login`: Log in and receive a JWT access token.
- **POST** `/auth/newAccessToken`: Get a new access token using a refresh token.
- **POST** `/auth/refreshToken`: Get a new access token and refresh token.
- **DELETE** `/auth/logout`: Log out by invalidating the refresh token.

### Task Endpoints

- **POST** `/task/new`: Create a new task (Admin only).
- **PUT** `/task`: Update a task (Admin only).
- **PUT** `/task/{taskId}/status`: Update task status (Admin or Executor).
- **DELETE** `/task`: Delete a task (Admin only).
- **GET** `/task/all`: Get all tasks.
- **GET** `/task/all/creator/{id}`: Get tasks by creator ID.
- **GET** `/task/all/executor/{id}`: Get tasks by executor ID.

### Comment Endpoints

- **POST** `/comments/task/{taskId}`: Post a new comment to a task (Admin or Executor).
- **GET** `/comments/task/{taskId}`: Get all comments for a task (Admin or Executor).

## Running the Application

To run the application, simply use the Docker Compose command:

```bash
docker-compose up --build
