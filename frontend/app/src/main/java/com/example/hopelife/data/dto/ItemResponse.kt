package com.example.hopelife.data.dto

import java.math.BigDecimal

data class ItemResponse(
        val produtoId: Long,
        val nome: String,
        val quantidade: Int,
        val precoUnitario: BigDecimal
    )