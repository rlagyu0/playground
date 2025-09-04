package commerce.api;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.web.DefaultSecurityFilterChain;

import javax.crypto.spec.SecretKeySpec;

import static org.springframework.security.oauth2.core.authorization.OAuth2AuthorizationManagers.hasScope;

@Configuration
public class SecurityConfiguration {

    @Bean
    PasswordEncoder passwordEncoder() {
        return Pbkdf2PasswordEncoder.defaultsForSpringSecurity_v5_8();
    }

    @Bean
    JwtKeyHolder jwtKeyHolder(@Value("${security.jwt.secret}") String jwtSecret) {
        SecretKeySpec key = new SecretKeySpec(jwtSecret.getBytes(), "HmacSHA256");
        return new JwtKeyHolder(key);
    }

    @Bean
    JwtDecoder jwtDecoder(JwtKeyHolder jwtKeyHolder) {
        return NimbusJwtDecoder.withSecretKey(jwtKeyHolder.key()).build();
    }

    @Bean
    DefaultSecurityFilterChain securityFilterChain(HttpSecurity http, JwtDecoder jwtDecoder) throws Exception {
        return http
            .csrf(AbstractHttpConfigurer::disable)
            .oauth2ResourceServer(c -> c.jwt(jwt -> jwt.decoder(jwtDecoder)))
            .authorizeHttpRequests(
                requests -> requests
                    .requestMatchers("/seller/signup").permitAll()
                    .requestMatchers("/shopper/signup").permitAll()
                    .requestMatchers("/seller/issuetoken").permitAll()
                    .requestMatchers("/shopper/issuetoken").permitAll()
                    .requestMatchers("/seller/**").access(hasScope("seller"))
                    .requestMatchers("/shopper/**").access(hasScope("shopper"))
                    .anyRequest().authenticated()
            )
            .build();
    }
}
