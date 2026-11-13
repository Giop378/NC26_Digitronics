# Digitronics

[![CI](https://github.com/Giop378/NC26_Digitronics/actions/workflows/ci.yml/badge.svg)](https://github.com/Giop378/NC26_Digitronics/actions/workflows/ci.yml)

Digitronics is a full-stack e-commerce web application for consumer electronics, written in **Java 21** with **Jakarta Servlets**, **JSP**, **MySQL** and **Maven**, and deployed on **Apache Tomcat 10.1**.

It was first developed as a team project for the Software Engineering course at the **University of Salerno**. It is now the subject of the **Software Dependability** course project, which applies the techniques from the [course labs](https://github.com/dardin88/swd-labs) (CI/CD, containerization, testing, coverage and mutation testing, JML, microbenchmarking, security analysis) to it.

---

## Quick start (Docker)

The only requirement is [Docker](https://docs.docker.com/get-docker/) with Docker Compose v2. You do not need to install a JDK, Maven, MySQL or Tomcat.

```bash
git clone https://github.com/Giop378/NC26_Digitronics.git
cd NC26_Digitronics

# 1. Create the database password secret (pick any value)
cp secrets/db_password.txt.example secrets/db_password.txt   # Windows: copy secrets\db_password.txt.example secrets\db_password.txt

# 2. Build and start the application and the database
docker compose up --build
```

Then open **<http://localhost:8080>**.

On first start, MySQL creates the `digitronics` database and loads the sample data from [`sql/`](sql/). The web container starts only after the database reports healthy.

| Task | Command |
|---|---|
| Start in the background | `docker compose up -d --build` |
| Follow the web app logs | `docker compose logs -f web` |
| Stop | `docker compose down` |
| Stop and **delete the database** (re-runs the SQL scripts next time) | `docker compose down -v` |

> The SQL scripts run only when the `db_data` volume is empty. After changing a file in `sql/`, run `docker compose down -v` to reload it.

### How the containers are set up

```text
            host :8080
                │
┌───────────────▼──────────────┐        ┌──────────────────────────┐
│ web                          │  JDBC  │ db                       │
│ tomcat:10.1-jdk21 (non-root) ├───────►│ mysql:8.4                │
│ ROOT.war                     │        │ volume: db_data          │
└──────────────────────────────┘        └──────────────────────────┘
         └──────── secret: db_password ────────┘
```

* [`Dockerfile`](Dockerfile) uses a multi-stage build: Maven builds the WAR, and only the WAR is copied into a Tomcat image that runs as an unprivileged `tomcat` user.
* [`docker-compose.yml`](docker-compose.yml) defines the `web` and `db` services, a named volume, health checks, `restart: on-failure`, and the database password as a Docker secret.

---

## Features

* User registration and authentication
* Product catalog, product details, search and category browsing
* Shopping cart management
* Checkout and order creation, order history
* Product reviews
* Administration area for product management

## Architecture

The backend is split into functional modules (`autenticazione`, `registrazione`, `gestioneCarrello`, `gestioneOrdine`, `gestioneProdotto`, `gestioneRecensione`, `infoProdotto`). Each module follows a layered architecture:

```text
HTTP request → Servlet (controller) → Service → DAO → MySQL
```

The presentation layer is built with JSP/JSTL, CSS and JavaScript.

```text
src/main/java/it/unisa/nc26/digitronics/
├── <module>/controller   # Servlets
├── <module>/service      # Business logic
├── model/bean            # Domain objects
├── model/dao             # JDBC data access
└── utils                 # Connection pool, init servlet, ...
src/main/webapp/          # JSP, CSS, JS, images
src/test/                 # JUnit / Mockito / H2 tests
sql/                      # MySQL schema and seed data
```

## Tech stack

| Area | Technologies |
|---|---|
| Backend | Java 21, Jakarta Servlet 6, JDBC, Tomcat JDBC pool |
| Frontend | JSP / JSTL, HTML, CSS, JavaScript |
| Database | MySQL 8 (H2 for integration tests) |
| Build | Maven (Maven Wrapper included) |
| Testing | JUnit 5 + JUnit 4 (vintage engine), Mockito 5, H2 |
| Runtime | Apache Tomcat 10.1, Docker, Docker Compose |
| CI | GitHub Actions |

---

## Configuration

The database connection is configured with environment variables. The defaults match a local MySQL installation.

| Variable | Default | Description |
|---|---|---|
| `DB_URL` | `jdbc:mysql://localhost:3306/digitronics?serverTimezone=<JVM timezone>` | JDBC URL |
| `DB_USER` | `root` | Database user |
| `DB_PASSWORD` | `root` | Database password |
| `DB_PASSWORD_FILE` | – | Path to a file containing the password, such as a Docker secret. Takes precedence over `DB_PASSWORD`. |

## Running without Docker

Requirements: JDK 21, MySQL 8, Apache Tomcat 10.1.

1. Create the database by running the scripts in this order:
   `sql/DigitronicsDB.sql`, `sql/AltriInserimenti.sql`, `sql/InserimentoProdotti.sql`, `sql/Utenti.sql`.
2. If your MySQL credentials are not `root`/`root`, set `DB_URL`, `DB_USER` and `DB_PASSWORD` in the environment Tomcat runs in (for example in `bin/setenv.sh` or `bin/setenv.bat`).
3. Build the WAR:
   ```bash
   ./mvnw clean package        # Windows: mvnw.cmd clean package
   ```
4. Copy `target/digitronics.war` to Tomcat's `webapps/` directory. Rename it to `ROOT.war` to serve the app on `/`.

## Testing

```bash
./mvnw clean verify
```

The suite contains unit tests (Mockito) and integration tests (in-memory H2 database) for the order, product and review modules. Test reports are written to `target/surefire-reports/`.

## Continuous integration

[`.github/workflows/ci.yml`](.github/workflows/ci.yml) runs `./mvnw clean verify` on JDK 21 for every push and pull request to `main`/`master`. It uploads the test reports and the WAR as build artifacts.

---

## Software Dependability project

Each lab report is a Markdown file in this repository and is linked here once completed.

| Lab | Topic | Report |
|---|---|---|
| 1 | CI/CD with GitHub Actions | `CICD.md` *(to do)* |
| 2a | Docker, Docker Compose | `Docker.md` *(to do)* |
| 2b | Orchestration with Minikube | *(to do)* |
| 3 | JUnit and Mockito | *(to do)* |
| 4 | JaCoCo, PiTest, Codecov | *(to do)* |
| 5 | JML and OpenJML | *(to do)* |
| 6 | JMH, JU2JMH, AMBER | *(to do)* |
| 7 | CodeQL, secret scanning, Dependabot | *(to do)* |
| 8 | GitGuardian, Snyk, SonarQube Cloud | *(to do)* |

### Notes and known limitations

* **HTTPS**: the original `web.xml` forced HTTPS (`CONFIDENTIAL` transport guarantee). This constraint was removed because the container serves plain HTTP on port 8080. In a real deployment, TLS should be terminated by a reverse proxy in front of Tomcat.
* Passwords are hashed with unsalted SHA-1, and the default database credentials in `ConPool` are hard-coded fallbacks. These are deliberately left as they are, so that the security analysis labs (7–8) can detect and address them.

## Project documentation

* [`projectDocs/`](projectDocs/): Software Engineering documentation (RAD, SDD, ODD, traceability matrix, testing material)
* [`docs/`](docs/): generated Javadoc

> All users and other data in the SQL seed files are dummy data created for testing purposes only.
