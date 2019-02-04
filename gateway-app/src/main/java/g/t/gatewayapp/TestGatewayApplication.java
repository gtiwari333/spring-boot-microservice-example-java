package g.t.gatewayapp;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
@EnableZuulProxy
public class TestGatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(TestGatewayApplication.class, args);
    }

    @Bean
    @LoadBalanced //this will search in the registry
    RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @RestController
    class GatewayAppController {
        private final RestTemplate restTemplate;

        GatewayAppController(RestTemplate restTemplate) {
            this.restTemplate = restTemplate;
        }

        @RequestMapping({"/", ""})
        public String testGateWayTime() {
            return "This is Gateway app. <a href='/gateway'>Open This </a> to see how this acts as single entry-point for API requests into an application from outside the firewall.";
        }

        @RequestMapping("/gateway")
        public String testGateWayGreeting() {
            JsonNode timeNode = restTemplate.getForObject("http://time-service/api/time/", JsonNode.class);
            String time = timeNode.get("servertime").asText();
            String greeting = restTemplate.getForObject("http://greeting-service/api/greeting/", String.class);

            return "The server says : " + greeting + ". Server time is " + time;
        }
    }
}

