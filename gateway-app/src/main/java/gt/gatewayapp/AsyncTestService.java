package gt.gatewayapp;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class AsyncTestService {

    @Async
    @SneakyThrows
    void verifyAsyncWillAccessSecurityContext() {
        Thread.sleep(2000);
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            log.error("Auth is null");
        } else {
            log.info("User auth object: {}", authentication);
        }
    }
}
