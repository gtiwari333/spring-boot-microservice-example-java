package gt.gatewayapp;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.task.DelegatingSecurityContextAsyncTaskExecutor;

@Configuration
@EnableAsync
public class AppConfig {
    AppConfig() {
        //now we can pass ThreadLocal + SecurityContext in @Async
        SecurityContextHolder.setStrategyName(SecurityContextHolder.MODE_INHERITABLETHREADLOCAL);
    }

    @Bean
    ThreadPoolTaskExecutor threadPoolTaskExecutor(){
        var executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(5);
        executor.setMaxPoolSize(20);
        executor.setThreadNamePrefix("app-threadX");
        executor.initialize();
        return executor;
    }

    /**
     * AsyncTaskExecutor(DelegatingSecurityContextAsyncTaskExecutor) bean can be used to pass ThreadLocal + SecurityContext in Future<></>
     */
    @Bean
    DelegatingSecurityContextAsyncTaskExecutor asyncTaskExecutor(ThreadPoolTaskExecutor ex){
        return new DelegatingSecurityContextAsyncTaskExecutor(ex);
    }
}
