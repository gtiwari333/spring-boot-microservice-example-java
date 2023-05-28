package gt.gatewayapp.clients;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.ZonedDateTime;
import java.util.Map;

@FeignClient(value = "time-service", fallback = FallbackTimeService.class)
public interface TimeService {

    @GetMapping({"/api/time"})
    Map<String, String> getTime(@RequestParam("delayMs") int delayMs);

}

@Component
@Slf4j
//it won't be the primary bean
class FallbackTimeService implements TimeService {

    @Override
    public Map<String, String> getTime(int delayMs) {
        log.debug("Current thread :{}", Thread.currentThread().getName());
        return Map.of("servertime", "Fallback to" + ZonedDateTime.now());
    }
}
