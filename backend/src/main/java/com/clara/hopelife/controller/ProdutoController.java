package com.clara.hopelife.controller;

import com.clara.hopelife.dto.ApiDtos.ProdutoView;
import com.clara.hopelife.service.CatalogoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/produtos")
@RequiredArgsConstructor
public class ProdutoController {
    private final CatalogoService service;

    @GetMapping
    List<ProdutoView> listar() {
        return service.listar();
    }

    @GetMapping("/favoritos")
    List<ProdutoView> favoritos(Authentication a) {
        return service.favoritos(a.getName());
    }

    @PutMapping("/{id}/favorito")
    ResponseEntity<Void> favoritar(Authentication a, @PathVariable Long id) {
        service.favoritar(a.getName(), id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}/favorito")
    ResponseEntity<Void> desfavoritar(Authentication a, @PathVariable Long id) {
        service.desfavoritar(a.getName(), id);
        return ResponseEntity.noContent().build();
    }
}
