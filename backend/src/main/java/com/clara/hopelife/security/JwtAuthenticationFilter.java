
package com.clara.hopelife.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;

    private static final org.slf4j.Logger logger =
        org.slf4j.LoggerFactory.getLogger(JwtAuthenticationFilter.class);
        
        public JwtAuthenticationFilter(JwtService jwtService) {
            this.jwtService = jwtService;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        String cabecalho = request.getHeader("Authorization");

        if (cabecalho == null || !cabecalho.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = cabecalho.substring(7);

        try {
            Claims claims = jwtService.validarToken(token);

            String email = claims.getSubject();
            String perfil = claims.get("perfil", String.class);

            if (email != null && perfil != null
                    && SecurityContextHolder.getContext()
                    .getAuthentication() == null) {

                var autenticacao =
                        new UsernamePasswordAuthenticationToken(
                                email,
                                null,
                                List.of(new SimpleGrantedAuthority(
                                        "ROLE_" + perfil
                                ))
                        );

                SecurityContextHolder.getContext()
                        .setAuthentication(autenticacao);
            }

        } catch (JwtException | IllegalArgumentException e) {
        SecurityContextHolder.clearContext();
        logger.warn("Falha ao validar JWT: {}", e.getMessage());
        }

        filterChain.doFilter(request, response);
    }
}
