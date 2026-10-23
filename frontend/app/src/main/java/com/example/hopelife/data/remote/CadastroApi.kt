package com.example.hopelife.data.remote

import com.example.hopelife.data.dto.CadastroRequest
import com.example.hopelife.data.dto.UsuarioResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface CadastroApi {
    @POST("usuarios")
    suspend fun cadastrar(
        @Body cadastro: CadastroRequest
    ): Response<UsuarioResponse>
}