## Microservice Example using Spring Boot and Spring Cloud.


### It contains the following applications:

- Spring Cloud Registration Service Discovery with Netflix Eureka (module: registration-discovery)
- Spring Cloud Config server (module: config-server)
- Admin Dashboard (module: admin-server-dashboard)
- Two Microservices:
    - Greeting Service (module: greeting-service)
    - Time Service (module: time-service)
- Gateway Application (module: gateway-app)
- Supports distributed Tracing with Zipkin (need to run the zipkin server app )
- Config for local development are stored on configs/local/**

## How to Run

Step 1) Start Keycloak and Zipkin using docker-compose. Navigate to /config and run `docker-compose up`

Step 2) Import the project into your IDE as maven project. Each of the following are Spring Boot Applications. Run the main classes from each module in following order:
- config-server
- registration-discovery
- greeting-service,   time-service  >> Any number of these services can be started. They will run on random port
- gateway-app
- admin-server-dashboard

Step 3) Open `http://localhost:8080/`  in your browser. 

Monitor the deployed service instances using Spring Eureka Web UI: `http://localhost:8761/`

View distributed tracing using zipkin: `http://localhost:9411`

For Admin Dashboard Open `http://localhost:8788' 