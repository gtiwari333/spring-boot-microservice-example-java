package gt.gatewayapp.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.ZonedDateTime;
import java.util.Map;

@FeignClient(value = "time-service", fallback = FallbackTimeService.class)
public interface TimeService {

    @GetMapping({"/api/time/"})
    Map<String, String> getTime();

}

@Component
//it won't be the primary bean
class FallbackTimeService implements TimeService {

    @Override
    public Map<String, String> getTime() {
        return Map.of("servertime", "Fallback to" + ZonedDateTime.now().toString());
    }
}
