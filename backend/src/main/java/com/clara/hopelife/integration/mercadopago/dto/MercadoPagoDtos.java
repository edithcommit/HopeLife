
package com.clara.hopelife.integration.mercadopago.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public final class MercadoPagoDtos {

    private MercadoPagoDtos() {
    }

    // Requisição para criar uma Order
    public record CriarOrderRequest(
            String type,

            @JsonProperty("total_amount")
            String totalAmount,

            @JsonProperty("external_reference")
            String externalReference,

            @JsonProperty("processing_mode")
            String processingMode,

            Payer payer,

            List<Item> items
    ) {
    }

    // Dados do comprador
    public record Payer(
            String email
    ) {
    }

    // Produtos enviados ao Mercado Pago
    public record Item(
            String title,
            Integer quantity,

            @JsonProperty("unit_price")
            String unitPrice
    ) {
    }

    
        @JsonIgnoreProperties(ignoreUnknown = true)
        public record CriarOrderResponse(
                String id,
                String status,
                @JsonProperty("checkout_url") String checkoutUrl,
                @JsonProperty("external_reference") String externalReference,
                @JsonProperty("status_detail") String statusDetail,
                @JsonProperty("total_paid_amount") String totalPaidAmount
        ) {}

}
