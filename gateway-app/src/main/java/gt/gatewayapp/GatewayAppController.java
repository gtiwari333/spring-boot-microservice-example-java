package gt.gatewayapp;

import gt.gatewayapp.clients.GreetingService;
import gt.gatewayapp.clients.TimeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.Map;

@RestController
class GatewayAppController {
    private final GreetingService greetingService;
    private final TimeService timeService;

    static final Logger log = LoggerFactory.getLogger(GatewayAppController.class);

    GatewayAppController(GreetingService greetingService, TimeService timeService) {
        this.greetingService = greetingService;
        this.timeService = timeService;
    }

    @RequestMapping({"/", ""})
    public String home() {
        return "Hello !<br/>" +
                "<a href='/protected'> Protected Service   </a> <br/>" +
                "<a href='/public'> Public   </a>  ";
    }

    @RequestMapping("/public")
    public String getPublicMessage() {
        return "No auth needed to see this";
    }

    @RequestMapping("/protected")
    public String getGreeting(Principal principal) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        log.info("User auth object: {}", authentication);

        Map<String, String> resp = timeService.getTime();
        String time = resp.get("servertime");

        String greeting = greetingService.getGreeting();

        return "The server says : " + greeting + ". Server time is " + time;
    }

    @GetMapping("/jwt")
    public String getToken(Principal principal) {

        Authentication authentication = (Authentication) principal;

        if (authentication instanceof OAuth2AuthenticationToken) {
            DefaultOidcUser user = (DefaultOidcUser) authentication.getPrincipal();
            OidcIdToken idToken = user.getIdToken();

            return "User = "+ user.getUserInfo().getSubject() + " , Token = " + idToken.getTokenValue();
        }

        return "NONE";
    }


    @GetMapping("/account")
    public Map<String, Object> getAccount(Principal principal) throws Exception {
        Authentication authentication = (Authentication) principal;

        if (authentication instanceof OAuth2AuthenticationToken) {
            return ((OAuth2AuthenticationToken) authentication).getPrincipal().getAttributes();
        } else {
            throw new Exception("User could not be found");
        }
    }


}
