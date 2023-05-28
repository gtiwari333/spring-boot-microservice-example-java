package gt.gatewayapp;

import gt.gatewayapp.clients.GreetingService;
import gt.gatewayapp.clients.TimeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.math.RandomUtils;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static java.util.concurrent.CompletableFuture.supplyAsync;

@RestController
@Slf4j
@RequiredArgsConstructor
class GatewayAppController {
    private final GreetingService greetingService;
    private final TimeService timeService;
    private final AsyncTestService asyncTestService;
    private final AsyncTaskExecutor asyncTaskExecutor;

    @RequestMapping({"/", ""})
    public String home() {
        return "Hello !<br/>" +
                "<a href='/protected'> Protected Service   </a> <br/>" +
                "<a href='/public'> Public   </a>  <br/> " +
                "<a href='/account'> View Full User Account Info   </a>  <br/> " +
                "<a href='/jwt'> View Jwt Auth Token   </a>   <br/>";

    }

    @RequestMapping("/public")
    public String getPublicMessage() {
        return "No auth needed to see this";
    }

    @RequestMapping("/protected-using-future")
    public String getGreetingUsingFutures() throws ExecutionException, InterruptedException {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        log.info("User auth object: {}", authentication);

        /*
        TODO: Trace propagation is not working ... security token propagation is working fine
         */

        var respF = supplyAsync(() -> timeService.getTime(getDelayMs()), asyncTaskExecutor);
        var greetingF = supplyAsync(() -> greetingService.getGreeting(getDelayMs()), asyncTaskExecutor);

        greetingService.getGreeting(getDelayMs());

        CompletableFuture.allOf(greetingF, respF).join();

        String time = respF.get().get("servertime");

        return "The server says : " + greetingF.get() + ". Server time is " + time;
    }


    @RequestMapping("/protected")
    public String getGreeting() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        log.info("User auth object: {}", authentication);

        var resp = timeService.getTime(getDelayMs());
        var greeting = greetingService.getGreeting(getDelayMs());

        greetingService.getGreeting(getDelayMs());

        String time = resp.get("servertime");

        return "The server says : " + greeting + ". Server time is " + time;
    }

    private static int getDelayMs() {
        return RandomUtils.nextInt(5000);
    }

    @RequestMapping("/async-test")
    void asyncTest() {
        log.info("Got async request");
        asyncTestService.verifyAsyncWillAccessSecurityContext();
    }

    @GetMapping("/jwt")
    public String getToken(Authentication auth) {
        if (auth instanceof OAuth2AuthenticationToken oAuth2Token) {
            DefaultOidcUser user = (DefaultOidcUser) oAuth2Token.getPrincipal();
            OidcIdToken idToken = user.getIdToken();
            return "User = " + user.getUserInfo().getSubject() + " , Token = " + idToken.getTokenValue();
        }
        return "NONE";
    }


    @GetMapping("/account")
    public Map<String, Object> getAccount(Authentication principal) throws Exception {
        if (principal instanceof OAuth2AuthenticationToken oAuth2Token) {
            return oAuth2Token.getPrincipal().getAttributes();
        } else {
            throw new Exception("User could not be found");
        }
    }


}
