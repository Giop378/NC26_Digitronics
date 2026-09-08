# Digitronics

Digitronics is a full-stack e-commerce web application developed in **Java 21** using **Jakarta Servlets**, **JSP**, **MySQL** and **Maven**.

The project was developed as part of a university software engineering course at the **University of Salerno** and focuses on the design, implementation and testing of a complete web application using a layered architecture.

## Features

The application includes the main features of an e-commerce platform:

* User registration and authentication
* Product catalog and product details
* Product search and category browsing
* Shopping cart management
* Checkout and order creation
* Order history
* Product reviews
* Administration features for product management

## Architecture

The backend is organized into functional modules such as:

* `autenticazione`
* `registrazione`
* `gestioneCarrello`
* `gestioneOrdine`
* `gestioneProdotto`
* `gestioneRecensione`
* `infoProdotto`

The application follows a layered architecture:

```text
HTTP Request
     |
     v
Servlet / Controller
     |
     v
Service
     |
     v
DAO
     |
     v
MySQL
```

The presentation layer is implemented with **JSP, CSS and JavaScript**.

## Tech Stack

**Backend**

* Java 21
* Jakarta Servlet API
* JDBC
* Apache Tomcat

**Frontend**

* JSP / JSTL
* HTML
* CSS
* JavaScript

**Database**

* MySQL

**Build & Dependency Management**

* Maven
* Maven Wrapper

**Testing**

* JUnit
* Mockito
* H2
* Unit testing
* Integration testing
* System testing

## Database

The database scripts are available in the [`sql`](sql/) directory.

The main schema can be created using:

```text
sql/DigitronicsDB.sql
```

Additional SQL files provide sample products, users and other seed data so that the application can be tested without manually populating the database.

> All users and other information contained in the SQL seed files are dummy data created exclusively for testing purposes.

## Running the Project

### Requirements

* JDK 21
* MySQL
* Apache Tomcat 10+

### Setup

Clone the repository:

```bash
git clone https://github.com/Giop378/NC26_Digitronics.git
cd NC26_Digitronics
```

Create the MySQL database using:

```text
sql/DigitronicsDB.sql
```

Then populate it using the additional scripts available in the `sql/` directory.

Configure the database connection according to your local MySQL installation.

Build the application using the Maven Wrapper:

```bash
./mvnw clean package
```

On Windows:

```powershell
mvnw.cmd clean package
```

Finally, deploy the generated WAR file on Apache Tomcat.

## Testing

The project includes automated tests for several core components of the application, including:

* Order management
* Product management
* Review management
* Controllers
* Services
* Integration scenarios using H2

The full Maven verification lifecycle can be executed with:

```bash
./mvnw clean verify
```

## Continuous Integration

The repository includes a Travis CI configuration that automatically executes:

```bash
./mvnw clean verify
```

This runs the build and automated test suite.

## Project Documentation

The [`projectDocs`](projectDocs/) directory contains the software engineering documentation produced during the project, including:

* Requirements and analysis documentation
* Software design documentation
* Traceability matrix
* Testing documentation
* System testing material

Generated Javadoc documentation is also available in the [`docs`](docs/) directory.

## Project Context

Digitronics was developed as a **team university project at the University of Salerno**.

The repository is maintained as part of my software development portfolio and documents the design, implementation and testing work carried out during the project.
