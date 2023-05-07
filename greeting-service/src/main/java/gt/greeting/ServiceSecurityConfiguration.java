package gt.greeting;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtDecoders;
import org.springframework.security.web.SecurityFilterChain;

@EnableWebSecurity
@Configuration
public class ServiceSecurityConfiguration {

    private static final String[] AUTH_WHITELIST = {
            "/swagger-resources/**",
            "/v3/api-docs/**",
            "/swagger-ui/**",
            "/favicon.ico",
            "/actuator",
            "/actuator/**",
            "/index.html",
            "/error",
            "/error/**",
            "/" //landing page is allowed for all
    };

    @Value("${spring.security.oauth2.client.provider.oidc.issuer-uri}")
    private String issuerUri;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        // @formatter:off
       return http
                .authorizeHttpRequests()
                .requestMatchers(AUTH_WHITELIST).permitAll()
                .and()
                    .authorizeHttpRequests()
                    .anyRequest().authenticated()
                .and()
                    .oauth2ResourceServer()
                    .jwt()
                    .and()
                .and()
                .build();
        // @formatter:on
    }

    @Bean
    JwtDecoder jwtDecoder() {
        return JwtDecoders.fromOidcIssuerLocation(issuerUri);
    }
}




