# LiteratiHub - Backend

## Overview

The backend of the LiteratiHub project handles all server-side operations, including user authentication, book management, and API endpoints. This section provides an overview of the backend architecture, the technologies used, and setup instructions.

## Technologies Used

- **Spring Boot 3**: A robust framework for building Java-based applications.
- **Spring Security 6**: Provides mechanisms for authentication and authorization to secure the application.
- **JWT Token Authentication**: Ensures secure communication between the client and server.
- **Spring Data JPA**: Simplifies data access and persistence with the Java Persistence API.
- **JSR-303 and Spring Validation**: Enables object validation using annotations.
- **OpenAPI and Swagger UI Documentation**: Generates API endpoint documentation.
- **Docker**: Facilitates the containerization of the backend application for deployment.

## Setup Instructions

To set up the backend of the LiteratiHub project, follow these steps:

1. Clone the repository:

   ```bash
   git clone https://github.com/nathsagar96/literati-hub.git
   ```

2. Run the Docker Compose file:

   ```bash
   docker-compose up -d
   ```

3. Navigate to the `literati-hub-api` directory:

   ```bash
   cd literati-hub
   cd literati-hub-api
   ```

4. Install dependencies (assuming Maven is installed):

   ```bash
   mvn clean install
   ```

5. Run the application (replace `x.x.x` with the current version from the `pom.xml` file):

   ```bash
   java -jar target/book-network-api-x.x.x.jar
   ```

6. Access the API documentation using Swagger UI:

   Open a web browser and go to `http://localhost:8088/swagger-ui/index.html`.

## Contributing

Feel free to contribute to the project by submitting pull requests or opening issues. Make sure to follow the coding standards and conventions used in the project.
