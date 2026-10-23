package com.example.hopelife.data.dto

data class CheckoutRequest(
        val itens: List<ItemCheckoutRequest>,
        val urlReceita: String? = null
    )
