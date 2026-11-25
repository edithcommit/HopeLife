
package com.example.hopelife.data.api

import com.example.hopelife.data.session.SessaoUsuario
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {

    private const val BASE_URL = "http://192.168.1.25:8080/"

    private val httpClient: OkHttpClient by lazy {
        OkHttpClient.Builder()
            .addInterceptor { chain ->

                val token = SessaoUsuario.obterToken()

                val request = chain.request().newBuilder().apply {

                    if (
                        !token.isNullOrBlank() &&
                        chain.request().header("Authorization") == null
                    ) {
                        header("Authorization", "Bearer $token")
                    }

                }.build()

                chain.proceed(request)
            }
            .build()
    }

    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(httpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val authApi: AuthApi by lazy {
        retrofit.create(AuthApi::class.java)
    }

    val produtoApi: ProdutoApi by lazy {
        retrofit.create(ProdutoApi::class.java)
    }

    val pedidoApi: PedidoApi by lazy {
        retrofit.create(PedidoApi::class.java)
    }
}
