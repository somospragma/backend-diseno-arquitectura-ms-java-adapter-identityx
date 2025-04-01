# IdentityX Controller

## Overview
The IdentityX Controller is a Spring Boot application designed to integrate with the IdentityX platform. It exposes REST endpoints to facilitate identity verification and authentication services, enabling secure and seamless verification operations.

## Table of Contents
- [Prerequisites](#prerequisites)
- [Installation](#installation)
- [Configuration](#configuration)
- [API Endpoints](#api-endpoints)
    - [Identity Verification](#identity-verification)
    - [Identity Validation](#identity-validation)
- [Usage](#usage)
    - [Example Request](#example-request)
    - [Example Response](#example-response)
- [Contributing](#contributing)
- [Security](#security)
- [License](#license)
- [Support](#support)

## Prerequisites
Before setting up the IdentityX Controller, ensure you have the following:

- **Java 11** or higher
- **Spring Boot** framework
- **Gradle** as a build tool
- **IdentityX SDK** (contact the IdentityX support for SDK details)

## Installation

### 1. Clone the repository:
```bash
git clone [repository-url]
cd [repository-name]
```

### 2. Install dependencies:
If you are using Gradle:
```bash
gradle build
```

## Configuration
To integrate the IdentityX Controller with your platform, configure the following properties:
#### 1. Open application.properties or application.yml and add the following configuration:

```bash
identityx:
  base-url: [your-identityx-base-url]
  api-key: [your-api-key]
  api-secret: [your-api-secret]
```
Replace [your-identityx-base-url], [your-api-key], and [your-api-secret] with your actual IdentityX credentials.

## API Endpoints

### Identity Verification

The controller provides the following endpoints for identity verification operations:

**POST** /api/v1/identity/verify

Initiates identity verification by providing the user's details.

### Request Body:

```bash
{
  "userId": "user123",
  "documentType": "PASSPORT",
  "documentNumber": "AB123456"
}
```

### Response:

```bash
{
  {
  "status": "SUCCESS",
  "verificationId": "ver-123-456",
  "timestamp": "2023-12-20T10:30:00Z"
}
```

**GET** /api/v1/identity/status/{id}
Checks the verification status of a given identity verification request.

### Response:

```bash
{
"status": "PENDING",
"verificationId": "ver-123-456",
"timestamp": "2023-12-20T10:30:00Z"
}
```

**POST** /api/v1/identity/validate
Validates identity documents submitted by the user.

### Request Body:

```bash
{
"documentType": "PASSPORT",
"documentNumber": "AB123456"
}
```
### Response:

```bash
{
"status": "VALID",
"documentType": "PASSPORT",
"documentNumber": "AB123456"
}
```

## Usage
### Example Request
### Identity Verification:
```bash
curl -X POST "http://localhost:8080/api/v1/identity/verify" \
-H "Content-Type: application/json" \
-d '{
"userId": "user123",
"documentType": "PASSPORT",
"documentNumber": "AB123456"
}'
```
### Example Response
```bash
{
"status": "SUCCESS",
"verificationId": "ver-123-456",
"timestamp": "2023-12-20T10:30:00Z"
}
```

## Contributing
We welcome contributions to the IdentityX Controller project. Please follow these steps to contribute:

 1. **Fork** the repository.

 2. **Create a new branch** for your feature:
    ```bash
    git checkout -b feature/amazing-feature
    ```
 3. **Commit your changes** with a clear message:

    ```bash
    git commit -m "Add some amazing feature"
    ```
 4. **Push your changes** to your fork:

    ```bash
    git push origin feature/amazing-feature
    ```
 5. **Open a Pull Request** to the main repository.

We will review your changes and merge them if they meet the project's standards.

## Security
To ensure the security of your integration:

- **API keys** should always be stored securely. Avoid hardcoding them in your source code.

- Use **HTTPS** for secure communication with the IdentityX platform.

- Follow security best practices for handling sensitive data such as user identities and documents.

## License
This project is licensed under the [LICENSE NAME] - see the LICENSE.md file for more details.

## Support
If you have any questions or need support, feel free to reach out.


