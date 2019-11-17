### Microservices system using Spring, Spring Boot and Spring Cloud.


#### It contains the following applications:

- Spring Cloud Registration Service Discovery with Netflix Eureka (module: registration-discovery)
- Spring Cloud Config server (module: config-server)
- Hystrix
- Two Microservices:
    - Greeting Service (module: greeting-service)
    - Time Service (module: time-service)
- Gateway Application (module: gateway-app)
- Supports distributed Tracing with

#### How to Run

Step 1) Import the project into your IDE as maven project. Each of the 4 modules are Spring Boot Applications. Run the main classes from each module in following order:

- registration-discovery
- greeting-service,   time-service  >> Any number of these services can be started. They will run on random port
- gateway-app

Step 2) Open `http://localhost:8080/` and `http://localhost:8080/amazing` in your browser. 

You can monitor the deployed service instances using Spring Eureka Web UI: `http://localhost:8761/`
