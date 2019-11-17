package gt.gatewayapp;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.Map;

@SpringBootApplication
@EnableZuulProxy
@EnableFeignClients
@EnableCircuitBreaker
public class TestGatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(TestGatewayApplication.class, args);
    }

    @Bean
    @LoadBalanced
        //this will search in the registry
    RestTemplate restTemplate(RestTemplateBuilder rtb) {
        return rtb.build();
    }

    @RestController
    static class GatewayAppController {
        private final GreetingService greetingService;
        private final TimeService timeService;

        GatewayAppController(GreetingService greetingService, TimeService timeService) {
            this.greetingService = greetingService;
            this.timeService = timeService;
        }

        @RequestMapping({"/", ""})
        public String home() {
            Map<String, String> resp = timeService.getTime();
            String time = resp.get("servertime");

            String greeting = greetingService.getGreeting();

            return "The server says : " + greeting + ". Server time is " + time;
        }


    }
}

@Service
class GreetingService {
    private final RestTemplate restTemplate;

    GreetingService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @HystrixCommand(fallbackMethod = "getDefaultGreeting")
    String getGreeting() {
        return restTemplate.getForObject("http://greeting-service/api/greeting/", String.class);
    }

    private String getDefaultGreeting() {
        return "Hello world";
    }

}

@FeignClient(value = "time-service", fallback = HystrixFallbackTimeService.class)
interface TimeService {

    @GetMapping({"/api/time/"})
    Map<String, String> getTime();

}

@Component
        //it won't be the primary bean
class HystrixFallbackTimeService implements TimeService {

    @Override
    public Map<String, String> getTime() {
        return Map.of("servertime", "Fallback to" + LocalDateTime.now().toString());
    }
}