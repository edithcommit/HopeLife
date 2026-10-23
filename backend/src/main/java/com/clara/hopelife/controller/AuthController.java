package com.clara.hopelife.controller;

import com.clara.hopelife.dto.ApiDtos.*;
import com.clara.hopelife.service.UsuarioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.access.prepost.PreAuthorize;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final UsuarioService service;

    @PostMapping("/clientes")
    ResponseEntity<UsuarioView> cliente(@Valid @RequestBody Cadastro d) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.cadastrar(d));
    }

    @PostMapping("/farmaceuticos")
    ResponseEntity<UsuarioView> farmaceutico(@Valid @RequestBody CadastroFarmaceutico d) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.cadastrarFarmaceutico(d));
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest dados) {
        return ResponseEntity.ok(service.login(dados.email(), dados.senha()));
    }

    @PostMapping("/login/farmaceuticos")
    public ResponseEntity<LoginResponse> loginFarmaceutico(
            @Valid @RequestBody LoginFarmaceuticoRequest dados) {
        return ResponseEntity.ok(service.loginFarmaceutico(dados.email(), dados.senha(), dados.matricula()));
    }

    @PutMapping("/perfil")
    @PreAuthorize("hasRole('CLIENTE')")
    public ResponseEntity<UsuarioView> atualizarPerfil(Authentication autenticacao, @Valid @RequestBody PerfilUpdate dados) {
        String email = autenticacao.getName();
        return ResponseEntity.ok(
                service.atualizar(email, dados));
    }

}
