# E-commerce Application with Spring Boot
This is a RESTful ecommerce backend application made by using Spring Boot.

* Java 11
* Spring Boot
* Spring Security
* JPA
* JUnit 5
* H2
* Swagger 2

## Objective
### Starbux Coffee - Backend API
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

## Solution
#### Authentication
Both authentication and authorization were implemented, so users can access their own data only and some operations can be restricted with respect to user roles.\
\
Authentication is made by using JWT (JSON Web Token). After register or login requests come and the user is authenticated, a token returns from the endpoint. From that on, all endpoints become available within the role boundaries.\
\
A token returns in response body after calling login or register endpoints. This token must be set as Authorization value in the header for other requests.\
\
Token timeout is hardcoded as one hour.

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
[POST] /api/cart/item
[DELETE] /api/cart/item/{id}
[DELETE] /api/cart/items
[GET] /api/products
[GET] api/order
[POST] api/order
```
##### Admin
```
[POST] /api/admin/product
[PUT] /api/admin/product/{id}
[DELETE] /api/admin/product/{id}
[GET] /api/admin/report
```
#### Request Sample
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
