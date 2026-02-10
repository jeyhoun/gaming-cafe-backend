# Gaming Cafe Backend - Development Guide

## Project Structure

```
gaming-cafe-backend/
├── src/main/java/az/gaming_cafe/
│   ├── client/           # External API integration
│   ├── config/           # Spring configurations (Security, JWT, etc.)
│   ├── controller/       # REST API endpoints
│   ├── exception/        # Custom exceptions and error handlers
│   ├── model/            # Entities, DTOs, Request/Response models
│   ├── repository/       # JPA Repositories
│   ├── security/         # RBAC, ABAC, JWT utilities
│   └── service/          # Business logic
├── devops/               # Docker compose files
├── postman/              # API test collection
└── docs/                 # Documentation and diagrams
```

## Tech Stack

- **Java 25**
- **Spring Boot 4.0.2**
- **PostgreSQL 15**
- **Spring Security** (JWT authentication)
- **Spring Data JPA**
- **WebSocket**
- **Maven**

## Getting Started

### 1. Prerequisites
- Java 25
- Maven 3.8+
- Docker & Docker Compose
- PostgreSQL client (optional)

### 2. Database Setup

```bash
# Start PostgreSQL container
cd devops
docker-compose up -d

# Check database status
docker-compose ps
```

### 3. Run Application

```bash
# Return to project root directory
cd ..

# Install dependencies and build
mvn clean install

# Start application
mvn spring-boot:run
```

**Alternative (with JAR):**
```bash
java -jar target/gaming-cafe-0.0.1-SNAPSHOT.jar
```

## Environment Variables (Optional)

```bash
export DB_USERNAME=gaming_cafe_user
export DB_PASSWORD=gaming_cafe_p@ssw@rd
```

## API Testing

### Postman Collection
Import `postman/gaming-cafe-back.postman_collection.json` into Postman.

### Default Endpoint
```
http://localhost:8080
```

## Code Quality

### Checkstyle
```bash
mvn checkstyle:check
```

### Tests
```bash
mvn test
```

## Database Schema

View database diagram: `docs/DB_MODEL.png`

## Useful Commands

```bash
# Build without tests
mvn clean install -DskipTests

# Stop database
docker-compose -f devops/docker-compose.yaml down

# Clean docker volumes
docker-compose -f devops/docker-compose.yaml down -v

# View logs
docker-compose -f devops/docker-compose.yaml logs -f
```

## Security Configuration

- JWT access token: 15 minutes (900000ms)
- JWT refresh token: 7 days (604800000ms)
- Max refresh token usage: 1 time

## Additional Information

- Default port: `8080`
- Database port: `5432`
- JPA DDL mode: `validate` (for production)
- Show SQL: `true` (development)
