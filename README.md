# MediDesk – A Dockerized Healthcare Application Deployed on AWS

<p align="center">
  <b>A Java Spring Boot Application with Docker, GitHub Actions CI/CD and AWS EC2 Deployment</b>
</p>

<p align="center">
  <img src="https://img.shields.io/badge/Java-21-orange?logo=openjdk" />
  <img src="https://img.shields.io/badge/Spring%20Boot-Framework-brightgreen?logo=springboot" />
  <img src="https://img.shields.io/badge/MySQL-8-blue?logo=mysql" />
  <img src="https://img.shields.io/badge/Docker-Containerized-blue?logo=docker" />
  <img src="https://img.shields.io/badge/AWS-EC2-orange?logo=amazonaws" />
  <img src="https://img.shields.io/badge/CI%2FCD-GitHub%20Actions-black?logo=githubactions" />
</p>

---

## 📌 Overview

**MediDesk** is a Dockerized healthcare web application developed as a group project by **Kumar Ankit and Gaurav Yadav**.

The project aims to support healthcare service-desk workflows through a centralized web application.

It is developed using Java 21 and Spring Boot, with MySQL 8 as the database. The application is containerized using Docker and deployed on Amazon Web Services (AWS) EC2.

The project also implements a CI/CD pipeline using GitHub Actions to automate application building, Docker image publishing, and cloud deployment.

### 🎯 Project Objectives

- Develop a centralized healthcare service-desk application.
- Build the backend using Java and Spring Boot.
- Manage application data using MySQL.
- Containerize the application using Docker.
- Automate builds and deployments using GitHub Actions.
- Host the application on AWS EC2.
- Implement secure cloud authentication using AWS IAM and GitHub OIDC.

---

## 🛠️ Technology Stack

| Technology | Purpose |
|---|---|
| Java 21 | Backend development |
| Spring Boot | Application framework |
| MySQL 8 | Database management |
| Maven Wrapper | Application build |
| Git | Version control |
| GitHub | Source code hosting |
| Docker | Application containerization |
| Docker Compose | Container management |
| GitHub Actions | CI/CD automation |
| GHCR | Docker image registry |
| AWS EC2 | Cloud hosting |
| AWS Systems Manager | Remote deployment |
| AWS IAM | Access management |
| GitHub OIDC | Secure AWS authentication |

---

## 🏗️ System Architecture

MediDesk follows a containerized deployment architecture hosted on AWS EC2.

### Architecture Overview

```text
                 USER
                   |
                   |
              Web Browser
                   |
                   | HTTP Request
                   | Port 80
                   |
             AWS EC2 INSTANCE
                   |
              Docker Engine
                   |
             Docker Compose
                   |
          +--------+--------+
          |                 |
          |                 |
    MediDesk App         MySQL 8
    Container           Container
          |                 |
          |                 |
     Spring Boot       Database
      Port 8081         Internal
          |
          |
      Application
       Response
          |
          |
        USER
```

### Request Flow

1. A user accesses MediDesk through a web browser.
2. The request reaches the AWS EC2 instance through port 80.
3. Docker forwards the request to the Spring Boot application running on port 8081 inside its container.
4. Spring Boot processes the request.
5. The application communicates with MySQL whenever database operations are required.
6. The response is returned to the user's browser.

**Note:** MySQL runs inside the Docker Compose network and is not exposed through a public host port.

---

## 📂 Project Structure

```text
MediDesk/
│
├── .github/
│   └── workflows/
│       └── CI/CD workflow files
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

| File / Directory | Description |
|---|---|
| `src/` | Contains application source code and resources |
| `.github/workflows/` | Contains GitHub Actions automation workflows |
| `.mvn/wrapper/` | Contains Maven Wrapper configuration |
| `Dockerfile` | Defines how the application Docker image is built |
| `docker-compose.yml` | Defines application and database containers |
| `pom.xml` | Contains Maven dependencies and build configuration |
| `mvnw` | Maven Wrapper script for Linux/macOS |
| `mvnw.cmd` | Maven Wrapper script for Windows |
| `.dockerignore` | Excludes unnecessary files from Docker build context |
| `.gitignore` | Specifies files Git should ignore |

---

## ⚙️ Prerequisites

Before running MediDesk, install the following:

- Java Development Kit (JDK) 21
- Git
- Docker Desktop or Docker Engine
- Docker Compose
- GitHub account (for repository access)

Check the installed versions:

```bash
java -version
```

```bash
git --version
```

```bash
docker --version
```

```bash
docker compose version
```

---

## 🚀 Getting Started

### Step 1: Clone the Repository

```bash
git clone YOUR_GITHUB_REPOSITORY_URL
```

Move into the project directory:

```bash
cd MediDesk
```

Replace the repository URL with the actual GitHub repository URL.

### Step 2: Build the Application

On Windows:

```bash
mvnw.cmd clean package -DskipTests
```

On Linux/macOS:

```bash
chmod +x mvnw
./mvnw clean package -DskipTests
```

This command compiles the Java application and generates the build artifact.

**Note:** The current build command uses `-DskipTests`, so automated tests are skipped.

### Step 3: Run Using Docker Compose

Make sure Docker is running.

Execute:

```bash
docker compose up -d --build
```

This command builds the required image and starts the services defined in the Docker Compose configuration.

### Step 4: Check Running Containers

```bash
docker compose ps
```

View application logs:

```bash
docker compose logs -f
```

### Step 5: Access the Application

Open the browser and visit:

```text
http://localhost
```

This assumes the Docker Compose port mapping is configured as host port 80 to container port 8081.

---

## 🐳 Docker Implementation

Docker is used to package the MediDesk application into a portable container image.

The Dockerfile defines the application image, while Docker Compose manages the application and database services.

### Useful Docker Commands

Build the application image:

```bash
docker compose build
```

Start containers:

```bash
docker compose up -d
```

Stop containers:

```bash
docker compose down
```

View running containers:

```bash
docker ps
```

View Docker images:

```bash
docker images
```

View application logs:

```bash
docker logs CONTAINER_NAME
```

---

## ☁️ AWS Cloud Deployment

MediDesk is deployed on **Amazon EC2 in the AWS Mumbai region (ap-south-1)**.

The EC2 instance runs Docker and Docker Compose to manage the application and database containers.

### AWS Services Used

**1. Amazon EC2**

Provides the virtual machine that hosts the MediDesk application.

**2. AWS IAM**

Controls permissions and access to AWS resources.

**3. AWS Systems Manager (SSM)**

Executes deployment commands remotely on the EC2 instance.

**4. AWS STS and GitHub OIDC**

Provide temporary AWS credentials to GitHub Actions without storing long-lived AWS access keys in GitHub.

**5. GitHub Container Registry (GHCR)**

Stores the Docker images generated by the CI/CD workflow.

### Deployment Process

1. New code is pushed to the GitHub main branch.
2. GitHub Actions builds the Spring Boot application.
3. Docker creates a new application image.
4. The image is published to GHCR.
5. GitHub Actions authenticates with AWS using OIDC.
6. AWS Systems Manager sends a deployment command to EC2.
7. EC2 pulls the latest application image.
8. Docker Compose updates the application container.
9. GitHub Actions checks the deployment command result.

---

## 🔄 CI/CD Pipeline

MediDesk uses GitHub Actions for Continuous Integration and Continuous Deployment.

### Pipeline Workflow

```text
Developer Pushes Code
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
    Docker Image Build
         |
         v
      Publish to GHCR
         |
         v
   AWS OIDC Authentication
         |
         v
   AWS Systems Manager
         |
         v
      AWS EC2 Server
         |
         v
   Pull Updated Image
         |
         v
   Docker Compose Update
         |
         v
   Deployment Result
```

### CI/CD Benefits

- Reduces manual deployment work.
- Automates application builds.
- Maintains version-specific Docker images.
- Provides consistent deployment environments.
- Uses temporary AWS credentials for authentication.
- Makes application updates easier to manage.

**Current limitation:** Automated tests are skipped in the existing Maven build command. Post-deployment health checks are also planned improvements.

---

## 🔐 Security Considerations

The project follows several security practices:

- AWS IAM controls access to cloud resources.
- GitHub OIDC provides temporary AWS credentials.
- AWS Systems Manager avoids the need for SSH-based deployment from GitHub Actions.
- MySQL is accessible through the internal Docker network rather than a publicly published host port.
- Docker separates the application and database into containers.

**Important:** Database passwords, SMTP credentials, AWS credentials, and other sensitive information should never be committed directly to the GitHub repository. Use GitHub Secrets, environment variables, or AWS Secrets Manager as appropriate.

The current public application endpoint uses HTTP. HTTPS configuration is a planned improvement.

---

## ⚠️ Current Limitations

Although MediDesk demonstrates containerization and automated cloud deployment, some improvements are still required:

1. The application currently runs on a single EC2 instance.
2. Automated tests are skipped in the current CI/CD workflow.
3. Post-deployment health checks are not yet implemented.
4. HTTPS and a custom domain are pending.
5. Database backup and recovery procedures need further improvement.
6. High availability and multi-instance deployment are not currently configured.

---

## 🔮 Future Enhancements

The following improvements can make MediDesk more scalable and reliable:

- Add automated unit and integration testing.
- Implement post-deployment health checks.
- Configure HTTPS using a domain and TLS certificate.
- Integrate Amazon CloudWatch for monitoring and logging.
- Move MySQL to Amazon RDS.
- Implement automated database backups.
- Introduce AWS Application Load Balancer.
- Use AWS ECS for container orchestration.
- Implement Docker image vulnerability scanning.
- Consider Kubernetes/EKS if future scaling requirements justify it.

---

## 👨‍💻 Project Development Team

MediDesk is a collaborative group project developed and implemented by **Gaurav Yadav and Kumar Ankit**.

Both team members contributed to the project development, implementation, and deployment activities, including application development, Docker containerization, DevOps automation, and AWS cloud deployment.

| Contributor | Role |
|---|---|
| Gaurav Yadav | Project Developer |
| Kumar Ankit | Project Developer |

**Project Title:** MediDesk – A Dockerized Healthcare Application Deployed on AWS

---

## 📚 Learning Outcomes

Through this project, we gained practical experience in:

- Java Spring Boot application development.
- MySQL database integration.
- Git and GitHub version control.
- Docker image creation and containerization.
- Docker Compose multi-container management.
- GitHub Actions CI/CD automation.
- GitHub Container Registry.
- AWS EC2 cloud deployment.
- AWS IAM and OIDC authentication.
- AWS Systems Manager remote deployment.

---

## 📄 License

This project is developed for academic and educational purposes.

---

**MediDesk – Building and Deploying Healthcare Applications with DevOps and Cloud Computing.**
