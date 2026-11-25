
package com.example.hopelife.data.repository

import com.example.hopelife.data.api.ProdutoApi
import com.example.hopelife.data.dto.ProdutoResponse
import retrofit2.HttpException

class ProdutoRepository(
    private val api: ProdutoApi
) {

    suspend fun listarProdutos(): List<ProdutoResponse> {
        val resposta = api.listarProdutos()

        if (!resposta.isSuccessful) {
            throw HttpException(resposta)
        }

        return resposta.body()
            ?: throw IllegalStateException(
                "O servidor retornou uma lista vazia."
            )
    }

    suspend fun listarFavoritos(): List<ProdutoResponse> {
        val resposta = api.listarFavoritos()

        if (!resposta.isSuccessful) {
            throw HttpException(resposta)
        }

        return resposta.body()
            ?: emptyList()
    }

    suspend fun favoritar(id: Long) {
        val resposta = api.favoritar(id)

        if (!resposta.isSuccessful) {
            throw HttpException(resposta)
        }
    }

    suspend fun desfavoritar(id: Long) {
        val resposta = api.desfavoritar(id)

        if (!resposta.isSuccessful) {
            throw HttpException(resposta)
        }
    }
}
