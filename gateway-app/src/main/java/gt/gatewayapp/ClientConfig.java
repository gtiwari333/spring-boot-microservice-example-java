package gt.gatewayapp;

import feign.RequestInterceptor;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.web.client.RestTemplate;

@Configuration
public class ClientConfig {

    @Bean
    public RequestInterceptor requestTokenBearerInterceptor() {
        return requestTemplate -> {
            //NOTE: hystrix.shareSecurityContext: true should be used to pass context
            requestTemplate.header(HttpHeaders.AUTHORIZATION, "Bearer " + getToken());
        };
    }

    @Bean
    @LoadBalanced
    RestTemplate buildTemplate(RestTemplateBuilder builder) {
        return builder.additionalInterceptors((rq, body, exe) -> {
            rq.getHeaders().add(HttpHeaders.AUTHORIZATION, "Bearer " + getToken());
            return exe.execute(rq, body);
        }).build();
    }

    private static String getToken() {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        Authentication authentication = securityContext.getAuthentication();

        if (authentication instanceof OAuth2AuthenticationToken) {
            DefaultOidcUser user = (DefaultOidcUser) authentication.getPrincipal();
            OidcIdToken idToken = user.getIdToken();

            return idToken.getTokenValue();
        }

        return "";
    }

}
