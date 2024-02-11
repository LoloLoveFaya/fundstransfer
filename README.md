# Fund Transfer API
The Fund Transfer API is a RESTful service built with Spring Boot that allows users to transfer funds between accounts with currency exchange. It supports concurrent invocation by multiple users or systems and includes functionality to retrieve exchange rates from external APIs.

# Features
- Transfer funds between accounts with currency conversion
- Retrieve exchange rates from external APIs
- Support for concurrent invocation
- Error handling for various scenarios such as insufficient balance and invalid account details

# Technologies Used
- Java
- Spring Boot
- OpenAPI (Swagger)
- Mockito
- JUnit

# Getting Started
To run the Fund Transfer API locally, follow these steps:
1. Clone this repository to your local machine.
2. Make sure you have Java and Maven installed.
3. Navigate to the project directory and run the following command to build the project:

```
mvn clean install
```
4. Start the application using the following command:
```
mvn spring-boot:run
````
5. The application will start locally at `http://localhost:8080`.

# API Documentation
The API documentation is available using Swagger UI. After starting the application, you can access the API documentation by visiting the following URL:
`http://localhost:8080/swagger-ui.html`

# Usage
Once the application is running, you can interact with the API using tools like cURL, Postman, or any HTTP client.

## Example Requests
Transfer Funds
To transfer funds between accounts, send a POST request to the `/api/transfers` endpoint with the following payload:
```
{
  "debitAccountId": 123,
  "creditAccountId": 456,
  "amount": 100.00,
  "currency": "USD"
}
````
Retrieve Account Balance
To retrieve the balance of an account, send a GET request to the `/api/accounts/{ownerId}` endpoint, replacing {ownerId} with the ID of the account owner.

# Testing
The project includes unit tests and integration tests to ensure the functionality works as expected. You can run the tests using the following command:
```
mvn test
````

# Contributing
Contributions to the Fund Transfer API are welcome! To contribute, please follow these steps:

1. Fork the repository.
2. Create a new branch for your feature or bug fix.
3. Make your changes and commit them to your branch.
4. Push your changes to your fork.
5. Submit a pull request to the main repository.
