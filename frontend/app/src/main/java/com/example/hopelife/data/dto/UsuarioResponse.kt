package com.example.hopelife.data.dto

data class UsuarioResponse(
        val id: Long,
        val nome: String,
        val email: String,
        val endereco: String?,
        val perfil: String
    )
