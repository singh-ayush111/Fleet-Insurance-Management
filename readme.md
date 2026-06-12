# Fleet Management System

A Spring Boot-based Fleet Management System for vehicle fleet operations, driver management, insurance claims, and corporate clients.

## Features

- Multi-role user system (Admin, Fleet Manager, Driver, Employee, Insurance Agent, Corporate Client)
- Fleet vehicle and driver management
- Insurance claims processing with status tracking
- Email notifications & document generation (PDF, Excel)
- JWT authentication with Spring Security
- Swagger/OpenAPI documentation
- Caching, rate limiting, async processing
- AOP-based logging & validation

## Technology Stack

- **Java 17** | **Spring Boot 3.5.14** | **Maven**
- **Database**: MySQL + JPA/Hibernate
- **Security**: Spring Security + JWT (jjwt 0.13.0)
- **API Docs**: Springdoc OpenAPI 2.8.16 (Swagger)
- **Libraries**: Lombok, Apache POI, iText PDF, Spring Mail, Spring Cache

## Project Structure

```
src/
├── main/
│   ├── java/com/htc/fleetmanagement/
│   │   ├── controller/        # REST API endpoints
│   │   ├── service/           # Business logic
│   │   ├── entity/            # JPA entities
│   │   ├── dto/               # Data Transfer Objects
│   │   ├── repository/        # Data access layer
│   │   ├── config/            # Spring configuration
│   │   ├── mapper/            # Entity to DTO mapping
│   │   ├── validator/         # Input validation
│   │   ├── exception/         # Custom exceptions
│   │   ├── filter/            # Security/HTTP filters
│   │   ├── logging/           # Logging utilities
│   │   ├── ratelimit/         # Rate limiting
│   │   └── util/              # Utility classes
│   └── resources/
│       ├── application.properties
│       └── templates/         # Email templates
└── test/
    └── java/com/htc/fleetmanagement/  # Unit tests
```

## Getting Started

### Prerequisites
- Java 17+
- Maven 3.8+
- MySQL 5.7+

### Installation

1. **Clone & Configure**
   ```bash
   git clone <repo-url> && cd fleetmanagement
   ```

2. **Update `application.properties`**
   ```properties
   spring.datasource.url=jdbc:mysql://localhost:3306/fleetmanagement_db
   spring.datasource.username=root
   spring.datasource.password=your_password
   spring.mail.host=your_mail_server
   app.jwt.secret=your_secret_key
   ```

3. **Build & Run**
   ```bash
   mvn clean install
   mvn spring-boot:run
   ```
   
   App runs on `http://localhost:5050`

## API Documentation

Swagger UI: `http://localhost:5050/swagger-ui.html`

## Available Controllers

- **AdminController** - Admin operations
- **AuthController** - Authentication and login
- **UserController** - User management
- **DriverController** - Driver management
- **FleetVehicleController** - Vehicle fleet management
- **FleetManagerController** - Fleet manager operations
- **FleetClaimController** - Insurance claims handling
- **CorporateClientController** - Corporate client management
- **InsuranceAgentController** - Insurance agent operations
- **EmployeeController** - Employee management
- **DriverFilterController** - Advanced driver filtering
- **PolicyTableController** - Insurance policy management

## Key Entities

- **User** - Base user entity with authentication
- **Admin** - Administrator role
- **Driver** - Vehicle drivers
- **FleetManager** - Fleet management personnel
- **Employee** - Organization employees
- **InsuranceAgent** - Insurance partners
- **CorporateClient** - Corporate clients
- **FleetVehicle** - Fleet vehicles
- **FleetClaim** - Insurance claims
- **PolicyTable** - Insurance policies

## Configuration Properties

| Property | Description |
|----------|-------------|
| `server.port` | Server port (default: 5050) |
| `spring.jpa.hibernate.ddl-auto` | Hibernate DDL strategy (update/create) |
| `app.jwt.secret` | JWT signing secret |
| `app.jwt.expiration` | JWT expiration in ms |

## Security

- **Authentication**: JWT-based token authentication
- **Authorization**: Role-based access control (RBAC)
- **Default Creds**: `admin` / `admin123` (**change in production!**)

## Building & Deployment

```bash
mvn clean package                    # Build JAR
mvn spring-boot:build-image          # Create Docker image
mvn test                             # Run tests
```

## Contributing

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit changes (`git commit -m 'Add AmazingFeature'`)
4. Push to branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

## Support

Open an issue in the repository for support

---

