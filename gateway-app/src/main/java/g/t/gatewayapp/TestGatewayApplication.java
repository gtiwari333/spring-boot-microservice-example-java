package g.t.gatewayapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@SpringBootApplication
@EnableZuulProxy
@EnableFeignClients
public class TestGatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(TestGatewayApplication.class, args);
    }

    @Bean
    @LoadBalanced
        //this will search in the registry
    RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @RestController
    static class GatewayAppController {
        private final RestTemplate restTemplate;
        private final TimeService timeService;

        GatewayAppController(RestTemplate restTemplate, TimeService timeService) {
            this.restTemplate = restTemplate;
            this.timeService = timeService;
        }

        @RequestMapping({"/", ""})
        public String home() {
            Map<String, String> resp = timeService.getTime();
            String time = resp.get("servertime");

            String greeting = restTemplate.getForObject("http://greeting-service/api/greeting/", String.class);

            return "The server says : " + greeting + ". Server time is " + time;
        }
    }
}

@FeignClient(value = "time-service")
interface TimeService {

    @GetMapping({"/api/time/"})
    Map<String, String> getTime();

}