
package com.example.hopelife.data.api

import com.example.hopelife.data.dto.ProdutoResponse
import retrofit2.Response
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PUT
import retrofit2.http.Path

interface ProdutoApi {

    @GET("produtos")
    suspend fun listarProdutos(): Response<List<ProdutoResponse>>

    @GET("produtos/favoritos")
    suspend fun listarFavoritos(): Response<List<ProdutoResponse>>

    @PUT("produtos/{id}/favorito")
    suspend fun favoritar(
        @Path("id") id: Long
    ): Response<Unit>

    @DELETE("produtos/{id}/favorito")
    suspend fun desfavoritar(
        @Path("id") id: Long
    ): Response<Unit>
}
