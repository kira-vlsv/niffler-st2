package guru.qa.niffler.config;

import guru.qa.niffler.service.cors.CorsCustomizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@EnableWebSecurity
@Configuration
@Profile("!local")
public class SecurityConfigMain {

    private final CorsCustomizer corsCustomizer;
    private final Environment environment;

    @Autowired
    public SecurityConfigMain(CorsCustomizer corsCustomizer, Environment environment) {
        this.corsCustomizer = corsCustomizer;
        this.environment = environment;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        corsCustomizer.corsCustomizer(http);

        http.authorizeHttpRequests()
                .requestMatchers("/actuator/health").permitAll()
                .anyRequest()
                .authenticated().and()
                .oauth2ResourceServer()
                .jwt();
        return http.build();
    }
}
