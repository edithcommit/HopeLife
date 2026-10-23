package com.example.hopelife.data.dto

data class PedidoResponse(
        val id: Long,
        val status: String,
        val urlReceita: String?,
        val dataCriacao: String?,
        val itens: List<ItemResponse>
    )
