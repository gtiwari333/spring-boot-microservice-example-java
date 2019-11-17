package gt.regdist;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.hystrix.dashboard.EnableHystrixDashboard;
import org.springframework.cloud.netflix.turbine.EnableTurbine;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@EnableHystrixDashboard
@EnableTurbine
@EnableDiscoveryClient
@SpringBootApplication
@Controller
public class HytrixDashboardApp {

    public static void main(String[] args) {
        SpringApplication.run(HytrixDashboardApp.class, args);
    }


    @RequestMapping("/")
    public String home() {
        return "forward:/hystrix";
    }
}

