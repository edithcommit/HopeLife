package com.clara.hopelife.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;

import javax.crypto.SecretKey;
import java.util.Date;

@Service
public class JwtService {

    private final SecretKey chave;

    public JwtService(
            @Value("${app.jwt.secret}") String segredoBase64) {

        byte[] segredo = Decoders.BASE64.decode(segredoBase64);
        this.chave = Keys.hmacShaKeyFor(segredo);
    }

    public String gerarToken(String email, String perfil) {
        Date agora = new Date();
        Date expiracao = new Date(
                agora.getTime() + 60 * 60 * 1000L
        );

        return Jwts.builder()
                .subject(email)
                .claim("perfil", perfil)
                .issuedAt(agora)
                .expiration(expiracao)
                .signWith(chave)
                .compact();
    }

    public Claims validarToken(String token) {
    return Jwts.parser()
            .verifyWith(chave)
            .build()
            .parseSignedClaims(token)
            .getPayload();
    }
}