# E-commerce Application with Spring Boot
This application is a simple ecommerce application made by using Spring Boot.

* Language: Java 11
* Framework: Spring Boot
* Database: H2

## Objective
### Starbux Coffee - Backend API\
We need a backend for our online coffee place startup, starbux coffee, where users can order
drinks/toppings and admins can create/update/delete drinks/toppings and have access to
reports.

#### Functional Requirements
> * Develop an API that will be used to order drinks with any of the topping combinations.
> * Visitor journeys should be transparent, the current amount of the cart and the products
should be communicated back to the caller of the API.
> * When finalizing the order, the original amount and the discounted amount should be
communicated back to the caller of the API.
> * Reports are present with the criteria suggested in the admin API requirements.
#### Drinks:
> * Black Coffee - 4 eur
> * Latte - 5 eur
> * Mocha - 6 eur
> * Tea - 3 eur
#### Toppings/sides:
> * Milk - 2 eur
> * Hazelnut syrup - 3 eur
> * Chocolate sauce - 5 eur
> * Lemon - 2 eur
#### Discount logic:
> * If the total of the cart is more than 12 euros, there should be a 25% discount.
> * If there are 3 or more drinks in the cart, the one with the lowest amount (including
toppings) should be free.
> * If the cart is eligible for both promotions, the promotion with the lowest cart amount
should be used and the other one should be ignored.
#### Admin api:
> * Should be able to create/update/delete products and toppings.
> * Reports:
> * Total amount of the orders per customer.
> * Most used toppings for drinks.

#### Other requirements:
> * Java 8+, spring boot, (any other library you need, i.e H2 database)
> * Test coverage > 70%

## Solution
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
