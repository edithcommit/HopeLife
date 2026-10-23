package com.example.hopelife.data.dto

import java.math.BigDecimal

data class ProdutoResponse(
        val id: Long,
        val nome: String,
        val preco: BigDecimal,
        val exigeReceita: Boolean
    )
