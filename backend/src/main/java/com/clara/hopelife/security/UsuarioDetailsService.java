package com.clara.hopelife.security;

import com.clara.hopelife.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UsuarioDetailsService implements UserDetailsService {
    private final UsuarioRepository usuarios;

    @Override
    public UserDetails loadUserByUsername(String email) {
        var u = usuarios.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado"));
        return User.withUsername(u.getEmail()).password(u.getSenhaHash()).roles(u.getPerfil().name()).build();
    }
}
