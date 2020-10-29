# E-commerce Application with Spring Boot
This application is a simple ecommerce application made by using Spring Boot.

* Language: Java 11
* Framework: Spring Boot
* Database: H2

Objective: We need a backend for our online coffee place startup, starbux coffee, where users can order
           drinks/toppings and admins can create/update/delete drinks/toppings and have access to
           reports.
           
#### Endpoints
##### Login
```
[POST] /api/auth/register
[POST] /api/auth/login
```
##### Customer
```
[GET] /api/cart
[POST] /api/cart
[DELETE] /api/cart
[DELETE] /api/cart/all
[GET] /api/products
[GET] api/order
[POST] api/order
```
##### Admin
```
[POST] /api/admin/product
[PATCH] /api/admin/product
[DELETE] /api/admin/product
[GET] /api/admin/report
```
#### Request Sample
All requests and responses are in JSON format. For example,
In order to create a new user, you need to send a POST request to:
http://localhost:8080/api/auth/register with a body like:
```
{
    "username": "paul.muaddib@arrakis.com",
    "firstName": "paul",
    "lastName": "muaddib",
    "userRole": "ADMIN",
    "password": "123456",
    "phoneNumber": "9876543210"
}
```
#### Authentication
We needed to add authentication and authorization controls, so users can access only their own data and some operations can be restricted with respect to user roles.
Authentication was made by using JWT (JSON Web Token). After register or login requests come and the user is authenticated, a token returns from the endpoint. From that on, all endpoints become available with that token except for the role restrictions.
Token timeout was set to an hour as hardcoded.

#### Exception Handling
All exceptions were handled in one place and provided returning the meaningful responses to the client.

#### API Documentation
Swagger 2 was implemented for API documentation.

#### Logging
Log4j2 was used for logging.

#### Report
Calculations for reporting is made on the fly when an admin makes a request to "/api/admin/report" endpoint.

#### Testing
Units and integration tests were implemented. Test coverage is:\
Class: %97\
Method: %85\
Line: %83