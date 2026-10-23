
package com.clara.hopelife.security;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    SecurityFilterChain filterChain(
            HttpSecurity http,
            JwtAuthenticationFilter jwtAuthenticationFilter
    ) throws Exception {

        return http
                .csrf(c -> c.disable())
                .sessionManagement(s -> s.sessionCreationPolicy(
                        SessionCreationPolicy.STATELESS
                ))
                .exceptionHandling(e -> e.authenticationEntryPoint(
                        (request, response, exception) ->
                                response.sendError(
                                        HttpServletResponse.SC_UNAUTHORIZED,
                                        "Não autenticado"
                                )
                ))
                .authorizeHttpRequests(a -> a
                        .requestMatchers(
                                HttpMethod.POST,
                                "/auth/clientes",
                                "/auth/farmaceuticos",
                                "/auth/login",
                                "/auth/login/farmaceuticos"
                                ).permitAll()
                        .requestMatchers(
                                "/produtos/favoritos",
                                "/produtos/*/favorito"
                        ).authenticated()
                        .requestMatchers(
                                HttpMethod.GET,
                                "/produtos",
                                "/produtos/**"
                        ).permitAll()
                        .anyRequest().authenticated()
                )
                .addFilterBefore(
                        jwtAuthenticationFilter,
                        UsernamePasswordAuthenticationFilter.class
                )
                .build();
    }
}
