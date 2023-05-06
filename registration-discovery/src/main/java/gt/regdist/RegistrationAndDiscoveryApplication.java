package gt.regdist;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;
import org.springframework.core.env.Environment;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;

@EnableEurekaServer
@SpringBootApplication
@Slf4j
public class RegistrationAndDiscoveryApplication {

    public static void main(String[] args) throws UnknownHostException {

        SpringApplication app = new SpringApplication(RegistrationAndDiscoveryApplication.class);
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
}

