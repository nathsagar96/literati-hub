# LiteratiHub

## Table of Contents

- [Overview](#overview)
- [Features](#features)
- [Technologies Used](#technologies-used)
  - [Backend](#backend)
  - [Frontend](#frontend)
- [Learnings](#learnings)
- [Getting Started](#getting-started)
- [Contributors](#contributors)
- [Acknowledgments](#acknowledgments)

## Overview

**LiteratiHub** is a dynamic full-stack application designed to empower users in managing their book collections and engaging with a vibrant community of book lovers. This application offers a range of features including user registration, secure email validation, and advanced book management capabilities such as creation, updating, sharing, and archiving. Additionally, users can borrow and return books with integrated availability checks and return approval processes. The system ensures robust security with JWT tokens and adheres to modern REST API best practices. The backend is powered by Spring Boot 3 and Spring Security 6, while the frontend leverages Angular and Tailwind CSS for a sleek, responsive user experience.

## Features

- **User Registration**: Seamless sign-up process for new users.
- **Email Validation**: Secure activation of accounts via email validation codes.
- **User Authentication**: Safe and secure login for returning users.
- **Book Management**: Comprehensive tools for creating, updating, sharing, and archiving books.
- **Book Borrowing**: Intelligent checks to ensure the availability of books for borrowing.
- **Book Returning**: Streamlined process for returning borrowed books.
- **Book Return Approval**: System for approving book returns.

## Technologies Used

### Backend

- **Spring Boot 3**: A powerful framework for developing Java-based backend services.
- **Spring Security 6**: Advanced security features for authentication and authorization.
- **JWT Token Authentication**: Secure token-based communication between client and server.
- **Spring Data JPA**: Simplifies data access and persistence using JPA.
- **JSR-303 and Spring Validation**: Facilitates object validation with annotations.
- **OpenAPI and Swagger UI**: Tools for generating interactive API documentation.
- **Docker**: Containerization technology for consistent deployment across environments.

### Frontend

- **Angular**: A robust framework for building responsive single-page applications (SPAs).
- **Tailwind CSS**: A utility-first CSS framework for creating custom, scalable designs.

## Learnings

Throughout this project, I have gained valuable experience in:

- Designing and implementing class diagrams based on business requirements.
- Adopting a mono repo approach for streamlined project management.
- Securing applications with JWT tokens and Spring Security.
- Managing user registration and email validation.
- Utilizing inheritance and object validation with Spring Data JPA and JSR-303.
- Developing service layers and handling application exceptions effectively.
- Configuring environment-specific settings using Spring Profiles.
- Documenting APIs with OpenAPI and Swagger UI.
- Crafting responsive user interfaces with Angular and Tailwind CSS.
- Dockerizing applications for consistent and reliable deployment.

## Getting Started

To begin working with the LiteratiHub project, follow the setup instructions for each component:

- [Backend Setup Instructions](literati-hub-api/README.md)
- [Frontend Setup Instructions](literati-hub-ui/README.md)

## Contributors

- [Sagar Nath](https://github.com/nathsagar96)

## Acknowledgments

Thanks to the developers and maintainers of the technologies used in this project. Their contributions make innovative projects like LiteratiHub possible.
