
package com.example.hopelife.data.session

import com.example.hopelife.data.dto.LoginResponse

object SessaoUsuario {

    @Volatile
    private var usuario: LoginResponse? = null

    fun salvarUsuario(dados: LoginResponse) {
        usuario = dados
    }

    fun obterUsuario(): LoginResponse? = usuario

    fun obterToken(): String? = usuario?.token

    fun limpar() {
        usuario = null
    }
}
