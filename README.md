# 🛡️ Vulnerability Scanner Web API

This project is a **Spring Boot Web API** that scans `package.json` files of Node.js projects and detects known vulnerabilities in the declared npm dependencies using the **GitHub Security Vulnerabilities GraphQL API**.

---

## ✨ Features

- Analyze `package.json` file content (Base64-encoded).
- Query GitHub’s GraphQL API for each dependency to identify vulnerabilities.
- Return detailed vulnerability information, including:
  - Affected package name
  - Installed version
  - Severity
  - First patched version (if available)
- Input validation and error handling with proper HTTP status codes.
- Packaged as a Docker container for deployment on AWS.
---

## 🧰 Technology Stack

- Java 21+
- Spring Boot
- Spring Web
- Spring Validation
- GitHub GraphQL API
- Docker

---

## ⚙️ Prerequisites

Before running the application, make sure you have:

- **Java 21 or higher** installed.
- A **GitHub Personal Access Token** with no scopes (for querying the GraphQL API).
- Docker installed (for containerization).

---

## 🚀 Running Locally
for running the project you need to:
- add environment variable:
- GITHUB_TOKEN = <your github api token>
- GITHUB_GRAPHQL_API = https://api.github.com/graphql (this is the current api that is used)
- Run the project with: mvn spring-boot:run
- The project is exposed on port 8080

---

## 🧪 Example Usage

### 📄 Request

**Endpoint:**
POST /api/v1/vulnerabilities/scan


**Request Body:**

```json
{
  "ecosystem": "npm",
  "fileContent": "ewogICJuYW1lIjogIk15IEFwcGxpY2F0aW9uIiwKICAidmVyc2lvbiI6ICIxLjAuMCIsCiAgImRlcGVuZGVuY2llcyI6IHsKICAgICJkZWVwLW92ZXJyaWRlIjogIjEuMC4xIiwKICAgICJleHByZXNzIjogIjQuMTcuMSIKICB9Cn0="
}
```

**Successful Response:**

```json
{
  "vulnerablePackages": [
    {
      "name": "deep-override",
      "version": "1.0.1",
      "severity": "CRITICAL",
      "firstPatchedVersion": "1.0.2"
    }
  ]
}
```
