# Art Collection Manager (ACM)

A web application for managing an art collection catalog, including artists, artworks, and images, with JWT-based authentication and role-based access control.

Developed as part of the **Full-Stack Spring Boot & Angular** training program at ENSI, based on the *Art Collection Manager* project specifications.

## Features

| Module | Feature | Status |
|---|---|---|
| Interface | Landing page (hero section, presentation, exhibitions, ecosystem) | Completed |
| Interface | Responsive navigation bar | Completed |
| Authentication | Login and registration pages with form validation | Completed (static) |
| Authentication | JWT authentication API, registration, login, and administrator email notification | Pending |
| Artists | View, create, update, and delete artists | Pending |
| Artworks | Gallery, artwork details, creation, update, and deletion | Pending |
| Users | User listing, role management, and deletion (ADMIN) | Pending |
| Security | Route guards, HTTP interceptor, and server-side role-based authorization | Pending |
| Deployment | Single `acm.jar` deployment on a VPS | Pending |

## User Roles and Permissions

| Action | COLLABORATOR | ADMIN |
|---|:---:|:---:|
| View artists and artworks | Yes | Yes |
| Create artists and artworks | Yes | Yes |
| Update and delete artists and artworks | No | Yes |
| Manage users and their roles | No | Yes |

All newly registered accounts are assigned the `COLLABORATOR` role by default. The `ADMIN` role can only be assigned by an existing administrator.

## Technology Stack

- **Frontend:** Angular (standalone components, signals, reactive forms), TypeScript, CSS
- **Backend:** Spring Boot 3, Java 17, Spring Web, Spring Data JPA, Spring Security, JWT (JJWT), Spring Mail
- **Database:** MySQL
- **Tools:** Maven, Postman, Git

## Architecture

The application follows a single-deployment architecture in which the Spring Boot application serves both the REST API and the compiled Angular frontend.

```text
Browser ──► acm.jar (Spring Boot: REST API + Angular frontend) ──► MySQL
                       └──► SMTP server (email notifications)
```

Images are not stored in the database or on the application server. Each artist and artwork record contains a public image URL.

### Frontend Architecture: Feature-Sliced Design

The frontend follows the Feature-Sliced Design (FSD) approach, with dependencies restricted to lower architectural layers:

`app → pages → widgets → features → entities → shared`

```text
frontend/src/app/
├── pages/        landing, login, register
├── widgets/      navbar, hero, about, events, ecosystem, auth-layout
├── features/     auth-login, auth-register
├── entities/     exhibition
├── shared/       ui/ (button, section-header)
└── styles/       tokens.css, base.css, forms.css
```

### Backend Architecture: Layered Architecture

The backend follows a layered architecture to separate HTTP handling, business logic, data access, and security concerns.

```text
backend/src/main/java/com/acm/
├── controller/   REST endpoints under /api
├── service/      business logic
├── repository/   data access (JPA)
├── entity/       User, Role, Artiste, Oeuvre
├── dto/          data transfer objects
├── security/     JWT authentication
├── config/       CORS and security configuration
└── exception/    global exception handling
```

## Installation and Setup

### Prerequisites

- Node.js 20.19 or later
- Angular CLI (`npm install -g @angular/cli`)
- Java 17
- Maven
- MySQL

### Frontend

```bash
cd frontend
npm install
ng serve
```

Open `http://localhost:4200` in your browser.

### Backend

The backend implementation is pending.

```bash
cd backend
./mvnw spring-boot:run
```

### Environment Configuration

Sensitive configuration values, including the JWT signing key, MySQL credentials, SMTP credentials, and initial administrator account, are provided through environment variables and are never committed to version control.


## Image Credits

The interface illustrations are sourced from the project's Figma design.

Artist and artwork images will be referenced through public URLs from public-domain or openly licensed sources. Their origins will be documented in the `credit` field.

## Author

**Nada Ben Abdelhafidh**

École Nationale des Sciences de l'Informatique (ENSI)
