### Microservices system using Spring, Spring Boot and Spring Cloud.


#### It contains the following applications:

- Spring Cloud Registration Service Discovery with Netflix Eureka (module: registration-discovery)
- Spring Cloud Config server (module: config-server)
- Hystrix Dashboard (module: hystrix-dashboard)
- Two Microservices:
    - Greeting Service (module: greeting-service)
    - Time Service (module: time-service)
- Gateway Application (module: gateway-app)
- Supports distributed Tracing with Zipkin (need to run the zipkin server app )
- Config for local development are stored on configs/local/**

#### How to Run

Step 1) Import the project into your IDE as maven project. Each of the 4 modules are Spring Boot Applications. Run the main classes from each module in following order:

- config-server
- registration-discovery
- greeting-service,   time-service  >> Any number of these services can be started. They will run on random port
- gateway-app

Step 2) Open `http://localhost:8080/`  in your browser. 

You can monitor the deployed service instances using Spring Eureka Web UI: `http://localhost:8761/`

View distributed tracing  `http://localhost:9411`

Hystrix Dashboard Open `http://localhost:8788' and monitor gateway app stream  `http://localhost:8080/actuator/hystrix.stream`
