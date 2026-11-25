
package com.example.hopelife.data.api

import com.example.hopelife.data.dto.CheckoutRequest
import com.example.hopelife.data.dto.PedidoResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface PedidoApi {

    @POST("pedidos")
    suspend fun criarPedido(
        @Header("Authorization") token: String,
        @Body dados: CheckoutRequest
    ): Response<PedidoResponse>

    @GET("pedidos/{id}")
    suspend fun buscarPedido(
        @Header("Authorization") token: String,
        @Path("id") pedidoId: Long
    ): Response<PedidoResponse>
}
