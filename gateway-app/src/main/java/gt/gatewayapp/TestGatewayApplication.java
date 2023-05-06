package gt.gatewayapp;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Map;

@SpringBootApplication
@EnableFeignClients
@Slf4j
public class TestGatewayApplication {

    public static void main(String[] args) throws UnknownHostException {

        SpringApplication app = new SpringApplication(TestGatewayApplication.class);
        Environment env = app.run(args).getEnvironment();

        log.info("Access URLs:\n----------------------------------------------------------\n\t" +
                        "Local: \t\t\thttp://localhost:{}\n\t" +
                        "External: \t\thttp://{}:{}\n\t" +
                        "Environment: \t{} \n" +
                        "----------------------------------------------------------",
                env.getProperty("local.server.port"),
                InetAddress.getLocalHost().getHostAddress(),
                env.getProperty("local.server.port"),
                Arrays.toString(env.getActiveProfiles())
        );
    }
    @Bean
    @LoadBalanced //this will search in the registry
    RestTemplate restTemplate(RestTemplateBuilder rtb) {
        return rtb.build();
    }

    @RestController
    @RequiredArgsConstructor
    static class GatewayAppController {
        private final GreetingService greetingService;
        private final TimeService timeService;

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
@Slf4j
@RequiredArgsConstructor
class GreetingService {
    private final RestTemplate restTemplate;

    @CircuitBreaker(name = "getGreeting", fallbackMethod = "getDefaultGreeting")
    String getGreeting() {
        return restTemplate.getForObject("http://greeting-service/api/greeting/", String.class);
    }

    public String getDefaultGreeting(Throwable t) {
        log.error("Using fallback method due to exception ", t);
        return "Hello world - Fallback";
    }

}

@FeignClient(value = "time-service", fallback = FallbackTimeService.class)
interface TimeService {

    @GetMapping({"/api/time/"})
    Map<String, String> getTime();

}

@Component //it won't be the primary bean
class FallbackTimeService implements TimeService {

    @Override
    public Map<String, String> getTime() {
        return Map.of("servertime", "Fallback to" + LocalDateTime.now());
    }
}