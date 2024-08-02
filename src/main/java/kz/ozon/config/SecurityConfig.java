package kz.ozon.config;

import lombok.SneakyThrows;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import static kz.ozon.constant.Constant.ADMIN;
import static kz.ozon.constant.Constant.USER;

@Configuration
public class SecurityConfig {

    @Bean
    @SneakyThrows
    public SecurityFilterChain securityFilterChain(final HttpSecurity httpSecurity) {
        return httpSecurity
                .authorizeHttpRequests(authorizationManagerRequestMatcherRegistry -> authorizationManagerRequestMatcherRegistry
                        .requestMatchers(
                                "/swagger-ui/**",
                                "/v3/api-docs/**",
                                "/swagger-resources/**",
                                "/swagger-ui.html",
                                "/webjars/**"
                        ).permitAll()
                        .requestMatchers("admin/users").permitAll()
                        .requestMatchers("/categories/**").permitAll()
                        .requestMatchers("admin/categories").hasAuthority(USER)
                        .requestMatchers("/users/**").hasRole(ADMIN)


                )
                .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}