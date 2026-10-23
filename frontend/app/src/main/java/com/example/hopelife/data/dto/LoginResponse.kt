
package com.example.hopelife.data.dto

data class LoginResponse(
    val id: Long,
    val nome: String,
    val email: String,
    val endereco: String?,
    val perfil: String,
    val token: String
)
