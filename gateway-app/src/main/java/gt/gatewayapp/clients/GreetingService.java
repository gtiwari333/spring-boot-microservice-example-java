package gt.gatewayapp.clients;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class GreetingService {

    private final RestTemplate restTemplate;

    GreetingService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @HystrixCommand(fallbackMethod = "getDefaultGreeting")
    public String getGreeting() {
        return restTemplate.getForObject("http://greeting-service/api/greeting/", String.class);
    }

    private String getDefaultGreeting() {
        return "Fallback Greeting";
    }

}
