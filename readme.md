### Microservices system using Spring, Spring Boot and Spring Cloud.


#### It contains the following 4 separate applications:

- Spring Cloud Registration and Discovery with Netflix Eureka (module: registration-discovery)
- Two Microservices:
    - Greeting Service (module: greeting-service)
    - Time Service (module: time-service)
- Gateway Application (module: gateway-app)


#### How to Run

Step 1) Import the project into your IDE as maven project. Each of the 4 modules are Spring Boot Applications. Run the main classes from each module in following order:

- registration-discovery
- greeting-service,   time-service
- gateway-app

Step 2) Open `http://localhost:8080/` and 'http://localhost:8080/amazing' in your browser. 

