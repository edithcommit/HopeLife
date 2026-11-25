
package com.clara.hopelife.integration.mercadopago;

import com.clara.hopelife.integration.mercadopago.dto.MercadoPagoDtos.CriarOrderRequest;
import com.clara.hopelife.integration.mercadopago.dto.MercadoPagoDtos.CriarOrderResponse;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
@RequiredArgsConstructor
public class MercadoPagoClient {

    private final RestClient mercadoPagoRestClient;

    public CriarOrderResponse criarOrder(
            CriarOrderRequest request,
            String chaveIdempotencia
    ) {

        return mercadoPagoRestClient
                .post()
                .uri("/v1/orders")
                .header("X-Idempotency-Key", chaveIdempotencia)
                .body(request)
                .retrieve()
                .body(CriarOrderResponse.class);
    }

    public CriarOrderResponse buscarOrder(String orderId) {
        return mercadoPagoRestClient.get()
                .uri("/v1/orders/{id}", orderId)
                .retrieve()
                .body(CriarOrderResponse.class);
    }

}
