
package com.clara.hopelife.controller;

import com.clara.hopelife.integration.mercadopago.MercadoPagoWebhookSignatureValidator;
import com.clara.hopelife.service.PedidoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/webhooks/mercadopago")
@RequiredArgsConstructor
public class MercadoPagoWebhookController {

    private final MercadoPagoWebhookSignatureValidator validator;
    private final PedidoService pedidoService;

    @PostMapping
    public ResponseEntity<Void> receberNotificacao(
            @RequestHeader(value = "x-signature", required = false)
            String assinatura,

            @RequestHeader(value = "x-request-id", required = false)
            String requestId,

            @RequestParam(value = "data.id", required = false)
            String dataId
    ) {

        boolean assinaturaValida = validator.validar(
                assinatura,
                requestId,
                dataId
        );

        if (!assinaturaValida) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        pedidoService.processarNotificacaoMercadoPago(dataId);

        return ResponseEntity.ok().build();
    }
}
