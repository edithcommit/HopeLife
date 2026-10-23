package com.clara.hopelife.controller;

import com.clara.hopelife.dto.ApiDtos.*;
import com.clara.hopelife.service.PedidoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/pedidos")
@RequiredArgsConstructor
public class PedidoController {
    private final PedidoService service;

    @PostMapping
    ResponseEntity<PedidoView> checkout(Authentication a, @Valid @RequestBody Checkout d) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.checkout(a.getName(), d));
    }

    @GetMapping("/pendentes-receita")
    @PreAuthorize("hasRole('FARMACEUTICO')")
    List<PedidoView> pendentes() {
        return service.pendentesReceita();
    }

    @PatchMapping("/{id}/aprovar-receita")
    @PreAuthorize("hasRole('FARMACEUTICO')")
    PedidoView aprovar(@PathVariable Long id) {
        return service.decidir(id, true);
    }

    @PatchMapping("/{id}/rejeitar-receita")
    @PreAuthorize("hasRole('FARMACEUTICO')")
    PedidoView rejeitar(@PathVariable Long id) {
        return service.decidir(id, false);
    }
}
