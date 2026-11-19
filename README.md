# MediDesk – Dockerized Healthcare Application on AWS
<p align="center">
  <strong>A Spring Boot Healthcare Application with Docker, GitHub Actions CI/CD and AWS EC2</strong>
</p>

<p align="center">
  <img src="https://img.shields.io/badge/Java-21-orange?logo=openjdk" alt="Java 21">
  <img src="https://img.shields.io/badge/Spring%20Boot-Backend-brightgreen?logo=springboot" alt="Spring Boot">
  <img src="https://img.shields.io/badge/MySQL-8.0-blue?logo=mysql" alt="MySQL">
  <img src="https://img.shields.io/badge/Docker-Containerization-blue?logo=docker" alt="Docker">
  <img src="https://img.shields.io/badge/AWS-EC2-orange?logo=amazonaws" alt="AWS EC2">
  <img src="https://img.shields.io/badge/GitHub%20Actions-CI%2FCD-black?logo=githubactions" alt="GitHub Actions">
</p>

---



---

## 1. Project Overview

**MediDesk** is a Dockerized healthcare web application developed as a group project by **Kumar Ankit and Gaurav Yadav**.

The project aims to support healthcare service-desk workflows through a centralized web application.

The backend is developed using Java 21 and Spring Boot, with MySQL 8.0 for database management. Docker and Docker Compose are used to containerize and manage the application and database services.

The application is deployed on Amazon Web Services (AWS) EC2. GitHub Actions automates the application build, Docker image publishing, and deployment process.

### Project Objectives

- Develop a centralized healthcare service-desk application.
- Build the backend using Java 21 and Spring Boot.
- Integrate MySQL for database management.
- Containerize the application using Docker.
- Manage multiple services using Docker Compose.
- Automate builds and deployments using GitHub Actions.
- Publish Docker images to GitHub Container Registry (GHCR).
- Deploy the application on AWS EC2.
- Use GitHub OIDC for secure AWS authentication.

## 2. Technology Stack

| Technology          | Purpose                    |
| ------------------- | -------------------------- |
| Java 21             | Backend programming        |
| Spring Boot         | Application framework      |
| MySQL 8.0           | Database                   |
| Maven Wrapper       | Build automation           |
| Git                 | Version control            |
| GitHub              | Source code hosting        |
| Docker              | Containerization           |
| Docker Compose      | Multi-container management |
| GitHub Actions      | CI/CD automation           |
| GHCR                | Docker image registry      |
| AWS EC2             | Cloud hosting              |
| AWS Systems Manager | Remote deployment          |
| AWS IAM             | Access management          |
| AWS STS             | Temporary credentials      |
| GitHub OIDC         | Secure AWS authentication  |
| Gmail SMTP          | Email integration          |

## 3. System Architecture

MediDesk follows a containerized architecture in which the Spring Boot application and MySQL database run as separate Docker containers.

### Application Architecture

```text
             USER
               |
               v
          Web Browser
               |
               v
           AWS EC2
               |
          Port 8081
               |
          Docker Engine
               |
          Docker Compose
               |
        +------+------+
        |             |
        v             v
   MediDesk App    MySQL 8.0
    Container       Container
        |             |
        v             v
   Spring Boot     medidesk_db
        |
        v
  Application Logic
        |
        v
   Database Queries
```

### Request Flow

1. A user accesses MediDesk through a web browser.
2. The request reaches the application through the configured EC2 endpoint.
3. Docker forwards the request to the Spring Boot application.
4. Spring Boot processes the request.
5. The application communicates with MySQL through the internal Docker network.
6. The response is returned to the user.

**Note:** MySQL is not published on a public host port. It communicates with the application through the Docker Compose network.

## 4. Project Structure

```text
MediDesk/
│
├── .github/
│   └── workflows/
│       └── CI-CD workflow
│
├── .mvn/
│   └── wrapper/
│
├── src/
│   ├── main/
│   │   ├── java/
│   │   └── resources/
│   │
│   └── test/
│
├── .dockerignore
├── .gitattributes
├── .gitignore
│
├── Dockerfile
├── docker-compose.yml
├── mvnw
├── mvnw.cmd
├── pom.xml
└── README.md
```

### Important Files

| File                 | Description                                |
| -------------------- | ------------------------------------------ |
| `src/`               | Application source code and resources      |
| `.github/workflows/` | GitHub Actions workflow                    |
| `Dockerfile`         | Defines the application Docker image       |
| `docker-compose.yml` | Defines application and database services  |
| `pom.xml`            | Maven dependencies and build configuration |
| `mvnw`               | Maven Wrapper for Linux/macOS              |
| `mvnw.cmd`           | Maven Wrapper for Windows                  |
| `.dockerignore`      | Excludes unnecessary Docker build files    |
| `.gitignore`         | Excludes unnecessary Git files             |

## 5. Prerequisites

Install the following tools before running MediDesk:

- Java Development Kit (JDK) 21
- Git
- Docker Desktop or Docker Engine
- Docker Compose

Verify the installations:

```bash
java -version
git --version
docker --version
docker compose version
```

## 6. Local Installation and Setup

### Step 1: Clone the Repository

```bash
git clone YOUR_GITHUB_REPOSITORY_URL
```

Move into the project directory:

```bash
cd MediDesk
```

Replace `YOUR_GITHUB_REPOSITORY_URL` with the actual repository URL.

### Step 2: Configure Environment Variables

Create a `.env` file in the project root directory.

```env
MYSQL_PASSWORD=your_database_password
MYSQL_ROOT_PASSWORD=your_root_password

MAIL_USERNAME=your_email@gmail.com
MAIL_PASSWORD=your_gmail_app_password
```

These environment variables configure the MySQL database and Gmail SMTP integration.

**Security Notes:**

- Never commit the `.env` file to GitHub.
- Use a Gmail App Password for SMTP authentication.
- Use strong database passwords.
- Keep all credentials private.

### Step 3: Build the Application

**Windows:**

```bash
mvnw.cmd clean package -DskipTests
```

**Linux/macOS:**

```bash
chmod +x mvnw
./mvnw clean package -DskipTests
```

This command compiles the application and generates the build artifact.

**Note:** Automated tests are currently skipped because the build command uses `-DskipTests`.

### Step 4: Start Docker Containers

Make sure Docker Desktop or Docker Engine is running.

```bash
docker compose up -d --build
```

Docker Compose starts:

- MediDesk application container.
- MySQL 8.0 database container.

The application waits for the MySQL health check before starting.

### Step 5: Verify Containers

```bash
docker compose ps
```

View logs:

```bash
docker compose logs -f
```

### Step 6: Access the Application

Open your browser:

```text
http://localhost:8081
```

The application uses host port 8081 mapped to container port 8081.

## 7. Docker Implementation

Docker packages MediDesk into a portable container, while Docker Compose manages the application and database services.

### Application Container

- Runs the Spring Boot application.
- Uses Java 21.
- Listens on port 8081.
- Communicates with MySQL through the Docker network.
- Uses a persistent directory mount for uploaded files.

### Database Container

- Uses the official MySQL 8.0 image.
- Creates the `medidesk_db` database.
- Uses the `medidesk_user` database user.
- Stores database files in a named Docker volume.
- Includes a health check to verify database availability.

### Persistent Storage

**MySQL Named Volume**

```yaml
volumes:
  - medidesk_mysql_data:/var/lib/mysql
```

This keeps database files persistent when containers are recreated.

**Application Upload Volume**

```yaml
volumes:
  - ./uploads:/app/uploads
```

This stores uploaded files outside the application container.

### Useful Docker Commands

Start services:

```bash
docker compose up -d
```

Stop services:

```bash
docker compose down
```

Rebuild application:

```bash
docker compose up -d --build
```

Check running containers:

```bash
docker ps
```

View Docker images:

```bash
docker images
```

View application logs:

```bash
docker logs medidesk-app
```

View database logs:

```bash
docker logs medidesk-db
```

**Important:** Avoid `docker compose down -v` unless you intentionally want to delete the named database volume and its stored data.

## 8. Email Configuration

MediDesk uses Gmail SMTP for email communication.

| Setting        | Configuration                      |
| -------------- | ---------------------------------- |
| SMTP Host      | smtp.gmail.com                     |
| SMTP Port      | 587                                |
| Authentication | Enabled                            |
| STARTTLS       | Enabled                            |
| Username       | Configured through `MAIL_USERNAME` |
| Password       | Configured through `MAIL_PASSWORD` |

Credentials are supplied through environment variables rather than being directly written into the Docker Compose file.

## 9. AWS Cloud Deployment

MediDesk is deployed on Amazon EC2 in the AWS Mumbai region.

**AWS Region:** `ap-south-1`

### AWS Services Used

**1. Amazon EC2**

Provides the virtual machine that runs the MediDesk application and MySQL containers.

**2. AWS IAM**

Controls permissions and access to AWS resources.

**3. AWS Systems Manager (SSM)**

Executes deployment commands remotely on the EC2 instance without requiring SSH access from the GitHub Actions runner.

**4. AWS STS**

Provides temporary security credentials for AWS authentication.

**5. GitHub OIDC**

Allows GitHub Actions to authenticate with AWS by assuming an IAM role.

**6. GitHub Container Registry (GHCR)**

Stores Docker images published by the CI/CD pipeline.

### Deployment Process

1. Developer pushes code to the `main` branch.
2. GitHub Actions builds the Spring Boot application.
3. Docker builds the application image.
4. The image is published to GHCR.
5. GitHub Actions obtains temporary AWS credentials using OIDC.
6. AWS Systems Manager receives the deployment command.
7. EC2 pulls the updated application image.
8. Docker Compose updates the application container.
9. GitHub Actions checks the remote command result.

## 10. CI/CD Pipeline Using GitHub Actions

MediDesk implements Continuous Integration and Continuous Deployment (CI/CD) using GitHub Actions.

### Workflow Triggers

- Push events on the `main` branch.
- Pull requests targeting the `main` branch.

### CI/CD Pipeline Stages

**Stage 1: Source Code Checkout**

GitHub Actions downloads the latest repository code.

**Stage 2: Java Setup**

The workflow configures Java 21 using Eclipse Temurin.

**Stage 3: Maven Build**

Maven Wrapper builds the application:

```bash
./mvnw --batch-mode --no-transfer-progress package -DskipTests
```

**Stage 4: Docker Buildx Setup**

Docker Buildx is configured to build the application image.

**Stage 5: GHCR Authentication**

On pushes to the `main` branch, GitHub Actions logs into GitHub Container Registry using `GITHUB_TOKEN`.

**Stage 6: Docker Image Publishing**

The application image is published with two tags:

```text
ghcr.io/kumarankit0109/medidesk:latest
```

```text
ghcr.io/kumarankit0109/medidesk:<commit-sha>
```

**Stage 7: AWS Authentication**

GitHub Actions uses OIDC to obtain temporary AWS credentials by assuming the configured IAM role.

**Stage 8: AWS Systems Manager Deployment**

AWS Systems Manager sends commands to the EC2 instance to pull the application image and update the application through Docker Compose.

**Stage 9: Deployment Status Check**

The workflow checks the remote SSM command status and reports success or failure.

### CI/CD Workflow Diagram

```text
       Developer
           |
           v
    GitHub Repository
           |
           v
     GitHub Actions
           |
           v
    Checkout Source Code
           |
           v
      Setup Java 21
           |
           v
      Maven Build
           |
           v
    Docker Buildx Setup
           |
           v
       Build Image
           |
           v
      Publish to GHCR
           |
           v
   GitHub OIDC Authentication
           |
           v
       AWS IAM Role
           |
           v
    AWS Systems Manager
           |
           v
         AWS EC2
           |
           v
        Pull Image
           |
           v
   Docker Compose Update
           |
           v
   Deployment Status Check
```

### CI/CD Trigger Behaviour

| Event                | Build | Publish Image | Deploy to EC2 |
| -------------------- | ----- | ------------- | ------------- |
| Push to main         | Yes   | Yes           | Yes           |
| Pull request to main | Yes   | No            | No            |

**Current Limitation:** The Maven build skips automated tests. A separate testing stage and post-deployment health check are recommended improvements.

## 11. Security Implementation

The project follows several security practices:

- AWS IAM controls permissions.
- GitHub OIDC provides temporary AWS credentials.
- AWS Systems Manager avoids SSH-based deployment from GitHub Actions.
- MySQL is not published through a public host port.
- Database and SMTP credentials are supplied through environment variables.
- Docker separates application and database services.

Sensitive values such as passwords and AWS credentials should never be committed to the public repository.

## 12. Current Limitations

The current implementation has some limitations:

1. The application runs on a single EC2 instance.
2. High availability and multi-instance deployment are not configured.
3. Automated tests are skipped during the Maven build.
4. Post-deployment application health checks are not implemented.
5. The public application endpoint currently uses HTTP.
6. Database backup and restoration procedures need further improvement.
7. A managed database service is not currently used.

## 13. Future Enhancements

The following improvements can make MediDesk more scalable, secure, and reliable:

- Add automated unit and integration testing.
- Implement post-deployment health checks.
- Configure HTTPS with a domain name and TLS certificate.
- Integrate Amazon CloudWatch for monitoring and logging.
- Move MySQL to Amazon RDS.
- Implement automated database backups and recovery testing.
- Introduce AWS Application Load Balancer.
- Use AWS ECS for container orchestration.
- Add Docker image vulnerability scanning.
- Consider Kubernetes/EKS if future project requirements justify it.

## 14. Project Contributors

| Name         | Contribution        |
| ------------ | ------------------- |
| Kumar Ankit  | Project Contributor |
| Gaurav Yadav | Project Contributor |

## 15. Learning Outcomes

Through this project, we gained practical experience in:

- Java 21 and Spring Boot development.
- MySQL database integration.
- Git and GitHub version control.
- Docker containerization.
- Docker Compose multi-container management.
- GitHub Actions CI/CD automation.
- GitHub Container Registry.
- AWS EC2 cloud deployment.
- AWS IAM and OIDC authentication.
- AWS Systems Manager remote deployment.
- Environment-based configuration and credential management.

---

## License

This project is developed for academic and educational purposes.

<p align="center">   \<strong>MediDesk\</strong>\<br>   Building and Deploying Healthcare Applications with DevOps and Cloud Computing. </p>
