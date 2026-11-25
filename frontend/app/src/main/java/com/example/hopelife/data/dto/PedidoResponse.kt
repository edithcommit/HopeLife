package com.example.hopelife.data.dto

import java.math.BigDecimal

data class PedidoResponse(
        val id: Long,
        val status: String,
        val urlReceita: String?,
        val dataCriacao: String?,
        val valorTotal: BigDecimal,
        val checkoutUrl: String?,
        val itens: List<ItemResponse>
)
