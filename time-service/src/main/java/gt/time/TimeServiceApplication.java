package gt.time;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Map;

@SpringBootApplication
@EnableDiscoveryClient
public class TimeServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(TimeServiceApplication.class, args);
    }


    @RestController
    @RequestMapping("/api/time")
    static class API {

        @Value("${app.timezone}")
        String timezone;

        @GetMapping({"", "/"})
        public Map<String, String> getMessage() {
            return Map.of("servertime", DateTimeFormatter.ISO_DATE_TIME.format(Instant.now().atZone(ZoneId.of(timezone))));
        }

    }
}
