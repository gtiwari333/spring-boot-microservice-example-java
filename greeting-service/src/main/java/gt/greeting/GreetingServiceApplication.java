package gt.greeting;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@SpringBootApplication
@EnableDiscoveryClient
public class GreetingServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(GreetingServiceApplication.class, args);
    }


    @RestController
    @RequestMapping("/api/greeting")
    static class API {

        @GetMapping({"", "/"})
        public String getMessage(Principal p) {
            return "Greetings from service -" + getUserName(p);
        }

        String getUserName(Principal p) {
            Authentication authentication = (Authentication) p;

            if (authentication instanceof JwtAuthenticationToken) {
                JwtAuthenticationToken jwt = (JwtAuthenticationToken) authentication;

                return jwt.getTokenAttributes().get("preferred_username") + "(" + jwt.getName() + ")";
            }

            return "N/A";
        }

    }
}

