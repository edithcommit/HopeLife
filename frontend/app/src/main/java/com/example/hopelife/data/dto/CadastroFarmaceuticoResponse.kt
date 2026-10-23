package com.example.hopelife.data.dto

data class CadastroFarmaceuticoResponse(
    val id: Long,
    val nome: String,
    val email: String,
    val endereco: String?,
    val perfil: String
)