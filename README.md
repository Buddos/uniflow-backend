# Uniflow Backend

A Spring Boot-based REST API backend for the Uniflow system - an educational management platform that handles academic operations, venue management, equipment requests, and live tracking.

## Overview

Uniflow Backend is built with **Spring Boot 3.5.13** and **Java 21**, providing a robust and secure API for managing educational resources, class scheduling, academic trips, makeup classes, and more. The system includes authentication, real-time updates via WebSocket, and comprehensive role-based access control.

## Tech Stack

- **Framework**: Spring Boot 3.5.13
- **Language**: Java 21
- **Database**: H2 (Development) / PostgreSQL (Production-ready)
- **Security**: Spring Security with JWT/OAuth2 support
- **API**: RESTful architecture
- **Real-time**: WebSocket support
- **Data Access**: Spring Data JPA
- **Admin Dashboard**: Spring Boot Admin integration
- **Build Tool**: Maven

## Features

- 🔐 **Authentication & Authorization** - Secure user authentication with role-based access control
- 📚 **Academic Management** - Manage courses, academic trips, and makeup classes
- 🏢 **Venue Management** - Track and manage educational venues
- 🛠️ **Equipment Management** - Handle equipment requests and allocation
- 📅 **Timetable Management** - Create and manage class schedules
- 🗺️ **Live Tracking** - Real-time location tracking for academic trips
- 🔔 **Notifications** - Real-time notification system via WebSocket
- 👥 **Role-Based Dashboards** - Separate interfaces for Admin, COD, and Lecturers

## Prerequisites

- **Java 21** or higher
- **Maven 3.6+**
- **Git**

## Installation & Setup

### 1. Clone the Repository

```bash
git clone <repository-url>
cd uniflow-backend
```

### 2. Build the Project

```bash
mvn clean compile
```

Or use the Maven wrapper:

```bash
./mvnw clean compile
```

### 3. Configure Application Properties

Edit `src/main/resources/application.yaml` to configure:

```yaml
server:
  port: 8080
  
spring:
  jpa:
    hibernate:
      ddl-auto: update
  datasource:
    url: jdbc:h2:mem:testdb
    driver-class-name: org.h2.Driver
```

### 4. Set Local Environment Variables

Before running the Spring application locally, set the database credentials in your shell.

PowerShell:

```powershell
$env:DB_USERNAME="postgres"
$env:DB_PASSWORD="password"
```

If you want to override the database URL as well, set `DATABASE_URL` in the same session.

## Running the Application

### Development Mode

```bash
mvn spring-boot:run
```

Or:

```bash
./mvnw spring-boot:run
```

The application will start on `http://localhost:8080`

### Building for Production

```bash
mvn clean package
```

This creates a JAR file in the `target/` directory that can be deployed.

## Project Structure

```
uniflow-backend/
├── src/
│   ├── main/
│   │   ├── java/com/uniflow/
│   │   │   ├── controller/        # REST API endpoints
│   │   │   ├── service/           # Business logic
│   │   │   ├── repository/        # Data access layer
│   │   │   ├── entity/            # JPA entities
│   │   │   ├── config/            # Spring configuration
│   │   │   ├── security/          # Security configuration
│   │   │   └── exception/         # Custom exceptions
│   │   └── resources/
│   │       ├── application.yaml   # Main configuration
│   │       ├── static/            # Static files
│   │       └── templates/         # Thymeleaf templates
│   └── test/
│       └── java/com/uniflow/      # Unit and integration tests
├── pom.xml                         # Maven configuration
└── target/                         # Compiled output
```

## API Endpoints

The API provides endpoints for:

- `POST /api/auth/login` - User authentication
- `POST /api/auth/logout` - User logout
- `GET /api/dashboard/*` - Dashboard data (role-specific)
- `GET/POST /api/courses` - Course management
- `GET/POST /api/venues` - Venue management
- `GET/POST /api/equipment` - Equipment requests
- `GET/POST /api/academic-trips` - Academic trip management
- `GET/POST /api/timetable` - Timetable management
- `WS /api/ws/notifications` - WebSocket for real-time notifications
- `WS /api/ws/live-tracking` - WebSocket for live location tracking

## Database

### H2 Console (Development)

Access the H2 database console at:

```
http://localhost:8080/h2-console
```

### Entity Management

Entities are auto-mapped using Spring Data JPA. Key entities include:

- `User` - User accounts with roles (ADMIN, COD, LECTURER, STUDENT)
- `Course` - Course information
- `Venue` - Physical locations
- `Equipment` - Equipment inventory
- `AcademicTrip` - Trip records
- `MakeupClass` - Makeup class schedules

## Security

- Spring Security with configurable authentication providers
- OAuth2 support for external authentication
- LDAP authentication available
- JWT token support for stateless authentication
- CORS configuration for frontend integration

## Testing

Run all tests:

```bash
mvn test
```

Run specific test class:

```bash
mvn test -Dtest=TestClassName
```

## Spring Boot Admin

Monitor application metrics and health:

```
http://localhost:8080/admin
```

**Note:** Configure credentials in `application.yaml`

## Environment Variables

Create a `.env` file or set system variables:

```
SPRING_DATASOURCE_URL=jdbc:postgresql://localhost:5432/uniflow
SPRING_DATASOURCE_USERNAME=postgres
SPRING_DATASOURCE_PASSWORD=password
JWT_SECRET=your-secret-key
```

## Troubleshooting

### Port Already in Use

```bash
# Windows
netstat -ano | findstr :8080

# Linux/Mac
lsof -i :8080
```

Change port in `application.yaml`:

```yaml
server:
  port: 8081
```

### Database Connection Issues

- Verify database is running
- Check `application.yaml` database URL
- Ensure credentials are correct

### Maven Build Failures

```bash
# Clear Maven cache
mvn clean
rm -rf ~/.m2/repository

# Rebuild
mvn install
```

## Contributing

1. Create a feature branch: `git checkout -b feature/feature-name`
2. Commit changes: `git commit -m 'Add feature'`
3. Push to branch: `git push origin feature/feature-name`
4. Submit a pull request

## Documentation

- [Spring Boot Documentation](https://spring.io/projects/spring-boot)
- [Spring Security Guide](https://spring.io/guides/gs/securing-web/)
- [Spring Data JPA](https://spring.io/projects/spring-data-jpa)
- [Spring Boot Admin](https://codecentric.github.io/spring-boot-admin/)

## License

This project is licensed under the MIT License - see LICENSE file for details.

## Support

For issues, questions, or contributions, please contact the development team or open an issue in the repository.

---

**Last Updated**: April 2026
