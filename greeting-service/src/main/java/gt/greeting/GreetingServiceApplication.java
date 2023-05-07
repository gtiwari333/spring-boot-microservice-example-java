package gt.greeting;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.core.env.Environment;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;

import java.security.Principal;

@SpringBootApplication
@EnableDiscoveryClient
@Slf4j
public class GreetingServiceApplication {

    public static void main(String[] args) throws UnknownHostException {

        SpringApplication app = new SpringApplication(GreetingServiceApplication.class);
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

    @RestController
    @RequestMapping("/api/greeting")
    @Slf4j
    static class API {

        @GetMapping({"", "/"})
        public String getMessage(Principal p) {
            String username = getUserName(p);
            log.info("Got request to get greeting message for " + username);
            return "Greetings -" + username + " !!";
        }

        String getUserName(Principal p) {
            if (p instanceof JwtAuthenticationToken jwt) {
                return jwt.getTokenAttributes().get("preferred_username") + "(" + jwt.getName() + ")";
            }

            return "N/A";
        }

    }
}

