package com.example.hopelife.data.repository

import com.example.hopelife.data.api.AuthApi
import com.example.hopelife.data.dto.CadastroFarmaceuticoRequest
import com.example.hopelife.data.dto.CadastroRequest
import com.example.hopelife.data.dto.UsuarioResponse
import retrofit2.HttpException
import com.example.hopelife.data.dto.PerfilUpdateRequest

class AuthRepository(
    private val api: AuthApi
) {

    suspend fun cadastrarCliente(
        dados: CadastroRequest
    ): UsuarioResponse {
        val resposta = api.cadastrarCliente(dados)

        if (!resposta.isSuccessful) {
            throw HttpException(resposta)
        }

        return resposta.body()
            ?: throw IllegalStateException(
                "O servidor retornou uma resposta vazia."
            )
    }

    suspend fun cadastrarFarmaceutico(
        dados: CadastroFarmaceuticoRequest
    ): UsuarioResponse {
        val resposta = api.cadastrarFarmaceutico(dados)

        if (!resposta.isSuccessful) {
            throw HttpException(resposta)
        }

        return resposta.body()
            ?: throw IllegalStateException(
                "O servidor retornou uma resposta vazia."
            )
    }

    suspend fun atualizarPerfil(
        token: String,
        dados: PerfilUpdateRequest
    ): UsuarioResponse {
        val resposta = api.atualizarPerfil(
            token = "Bearer $token",
            dados = dados
        )

        if (!resposta.isSuccessful) {
            throw HttpException(resposta)
        }

        return resposta.body()
            ?: throw IllegalStateException(
                "O servidor retornou uma resposta vazia."
            )
    }
}