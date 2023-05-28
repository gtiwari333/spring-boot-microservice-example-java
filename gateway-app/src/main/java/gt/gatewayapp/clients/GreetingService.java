package gt.gatewayapp.clients;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

@Service
@Slf4j
public class GreetingService {

    private final RestTemplate restTemplate;

    GreetingService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @CircuitBreaker(name = "getGreeting", fallbackMethod = "getDefaultGreeting")
    public String getGreeting(@RequestParam int delayMs) {
        log.debug("Current thread :{}", Thread.currentThread().getName());
        return restTemplate.getForObject("http://greeting-service/api/greeting?delayMs=" + delayMs, String.class);
    }

    public String getDefaultGreeting(Throwable t) {
        log.error("Using fallback method due to exception ", t);
        return "Hello world - Fallback";
    }
}
