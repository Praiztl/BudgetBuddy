
# BudgetBuddy
## Overview
BudgetBuddy is designed to streamline and automate budget allocation for companies

## Features
#### User Management: Create, retrieve, update, and delete users. 
#### JWT-based Authentication: Secure API endpoints with JSON Web Tokens. 
#### OTP Verification: Implement OTP-based user verification. 

## Project Structure
#### Controllers: Handles incoming HTTP requests and routes them to the appropriate service layer. 
#### Services: Contains the business logic of the application. 
#### DTOs: Data Transfer Objects used for transferring data between layers. 
#### Repositories: Interfaces for interacting with the database. 
#### Security: Handles authentication and authorization using JWT. 

## How to Run
### Clone the repository:
#### git clone https://github.com/praiztl/BudgetBuddy.git   
#### cd BudgetBuddy 

### Build and run the application:
#### mvn clean install  
#### mvn spring-boot:run

### Access the API:
#### Registration: POST /api/v1/auth/register 
#### Login: POST /api/v1/auth/login 


## Testing
#### You can test the API endpoints using tools like Postman or curl. Make sure to include the JWT token in the Authorization header when accessing secured endpoints.

## Troubleshooting
#### Mail Server Issues: If you encounter MailSendException or MailAuthenticationException, ensure your mail server credentials are correct and the server is accessible. 
#### JWT Issues: Ensure your secret key is at least 256 bits long and properly configured in the application.properties.
