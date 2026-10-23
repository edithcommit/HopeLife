package com.example.hopelife.data.dto

data class PerfilUpdateRequest(
        val nome: String,
        val endereco: String? = null,
        val senha: String? = null
    )
