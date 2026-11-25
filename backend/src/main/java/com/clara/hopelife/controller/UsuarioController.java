package com.clara.hopelife.controller;

import com.clara.hopelife.dto.ApiDtos.*;
import com.clara.hopelife.service.UsuarioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/usuarios")
@RequiredArgsConstructor
public class UsuarioController {
    private final UsuarioService service;

    @PutMapping("/me")
    UsuarioView atualizar(Authentication a, @Valid @RequestBody PerfilUpdate d) {
        return service.atualizar(a.getName(), d);
    }

    @DeleteMapping("/me")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void excluirConta(Authentication a) {
        service.excluirConta(a.getName());
    }

}
