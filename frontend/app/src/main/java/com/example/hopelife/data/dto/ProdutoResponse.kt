
package com.example.hopelife.data.dto

import java.math.BigDecimal

data class ProdutoResponse(
    val id: Long,
    val nome: String,
    val preco: BigDecimal,
    val exigeReceita: Boolean,
    val descricao: String?,
    val imagemUrl: String?,
    val categoria: String?,
    val quantidadeEstoque: Int,
    val ativo: Boolean
)
