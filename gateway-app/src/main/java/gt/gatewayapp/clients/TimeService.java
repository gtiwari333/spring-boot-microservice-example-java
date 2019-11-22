package gt.gatewayapp.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.LocalDateTime;
import java.util.Map;

@FeignClient(value = "time-service", fallback = HystrixFallbackTimeService.class)
public interface TimeService {

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
