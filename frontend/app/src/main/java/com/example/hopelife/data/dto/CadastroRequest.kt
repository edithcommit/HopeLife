package com.example.hopelife.data.dto

data class CadastroRequest(
    val nome: String,
    val email: String,
    val senha: String,
    val endereco: String? = null
)

