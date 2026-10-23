package com.example.hopelife.data.api

import com.example.hopelife.data.dto.CadastroFarmaceuticoRequest
import com.example.hopelife.data.dto.CadastroRequest
import com.example.hopelife.data.dto.LoginRequest
import com.example.hopelife.data.dto.LoginResponse
import com.example.hopelife.data.dto.UsuarioResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import com.example.hopelife.data.dto.PerfilUpdateRequest
import retrofit2.http.Header
import retrofit2.http.PUT

interface AuthApi {

    @POST("auth/clientes")
    suspend fun cadastrarCliente(
        @Body dados: CadastroRequest
    ): Response<UsuarioResponse>

    @POST("auth/farmaceuticos")
    suspend fun cadastrarFarmaceutico(
        @Body dados: CadastroFarmaceuticoRequest
    ): Response<UsuarioResponse>

    @POST("auth/login")
    suspend fun login(
        @Body dados: LoginRequest
    ): Response<LoginResponse>

    @POST("auth/login/farmaceuticos")
    suspend fun loginFarmaceutico(
        @Body dados: LoginRequest
    ): Response<LoginResponse>

    @PUT("auth/perfil")
    suspend fun atualizarPerfil(
        @Header("Authorization") token: String,
        @Body dados: PerfilUpdateRequest
    ): Response<UsuarioResponse>
}