
package com.example.hopelife.ui.carrinho

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hopelife.data.api.RetrofitClient
import com.example.hopelife.data.dto.CheckoutRequest
import com.example.hopelife.data.dto.ItemCheckoutRequest
import com.example.hopelife.data.session.SessaoUsuario
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.io.IOException
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay

data class ItemCarrinho(
    val produto: com.example.hopelife.data.dto.ProdutoResponse,
    val quantidade: Int = 1
)

data class CheckoutUiState(
    val carregando: Boolean = false,
    val mensagem: String? = null,
    val checkoutUrl: String? = null,
    val pedidoId: Long? = null
)

class CarrinhoViewModel : ViewModel() {

    private val _itens = MutableStateFlow<List<ItemCarrinho>>(emptyList())
    val itens = _itens.asStateFlow()

    private val _checkoutState = MutableStateFlow(CheckoutUiState())
    val checkoutState = _checkoutState.asStateFlow()

    private var monitoramentoPagamentoJob: Job? = null

    fun adicionarProduto(produto: com.example.hopelife.data.dto.ProdutoResponse): Boolean {
        if (produto.quantidadeEstoque <= 0) return false

        val listaAtual = _itens.value.toMutableList()
        val indice = listaAtual.indexOfFirst { it.produto.id == produto.id }

        if (indice >= 0) {
            val itemAtual = listaAtual[indice]
            if (itemAtual.quantidade >= produto.quantidadeEstoque) return false

            listaAtual[indice] = itemAtual.copy(
                quantidade = itemAtual.quantidade + 1
            )
        } else {
            listaAtual.add(ItemCarrinho(produto = produto))
        }

        _itens.value = listaAtual
        return true
    }

    fun aumentarQuantidade(produtoId: Long) {
        atualizarQuantidade(produtoId, 1)
    }

    fun diminuirQuantidade(produtoId: Long) {
        val item = _itens.value.find { it.produto.id == produtoId } ?: return

        if (item.quantidade <= 1) {
            removerProduto(produtoId)
        } else {
            atualizarQuantidade(produtoId, -1)
        }
    }

    private fun atualizarQuantidade(produtoId: Long, incremento: Int) {
        _itens.value = _itens.value.map { item ->
            if (item.produto.id == produtoId) {
                val novaQuantidade = (item.quantidade + incremento)
                    .coerceIn(1, item.produto.quantidadeEstoque)

                item.copy(quantidade = novaQuantidade)
            } else {
                item
            }
        }
    }

    fun removerProduto(produtoId: Long) {
        _itens.value = _itens.value.filter { it.produto.id != produtoId }
    }

    fun limparCarrinho() {
        _itens.value = emptyList()
    }

    fun finalizarCompra() {
        val itensAtuais = _itens.value

        if (itensAtuais.isEmpty()) {
            _checkoutState.value = CheckoutUiState(
                mensagem = "Seu carrinho está vazio."
            )
            return
        }

        if (_checkoutState.value.carregando) return

        val token = SessaoUsuario.obterToken()

        if (token.isNullOrBlank()) {
            _checkoutState.value = CheckoutUiState(
                mensagem = "Sua sessão expirou. Entre novamente."
            )
            return
        }

        viewModelScope.launch {
            _checkoutState.value = CheckoutUiState(carregando = true)

            try {
                val dadosCheckout = CheckoutRequest(
                    itens = itensAtuais.map { item ->
                        ItemCheckoutRequest(
                            produtoId = item.produto.id,
                            quantidade = item.quantidade
                        )
                    }
                )

                val resposta = RetrofitClient.pedidoApi.criarPedido(
                    token = "Bearer $token",
                    dados = dadosCheckout
                )

                if (resposta.isSuccessful) {
                    val pedido = resposta.body()

                    if (pedido == null) {
                        _checkoutState.value = CheckoutUiState(
                            mensagem = "O servidor não retornou os dados do pedido."
                        )
                    } else {
                        val url = pedido.checkoutUrl
                            ?.takeIf { it.isNotBlank() }

                        val mensagem = when {
                            url != null -> "Pedido criado! Abrindo pagamento..."
                            pedido.status == "PENDENTE_RECEITA" ->
                                "Pedido criado! Aguardando análise da receita."
                            else ->
                                "Pedido criado, mas não foi retornado um link de pagamento."
                        }

                        _checkoutState.value = CheckoutUiState(
                            mensagem = mensagem,
                            checkoutUrl = url,
                            pedidoId = pedido.id
                        )

                        if (url != null) {
                            monitorarPagamento(
                                pedidoId = pedido.id,
                                token = token,
                                itensDoPedido = itensAtuais
                            )
                        }
                    }
                } else {
                    val mensagem = when (resposta.code()) {
                        401 -> "Sua sessão expirou. Entre novamente."
                        403 -> "Você não tem permissão para realizar este pedido."
                        400 -> "Não foi possível validar os produtos do pedido."
                        else -> "Não foi possível criar o pedido. Código: ${resposta.code()}."
                    }

                    _checkoutState.value = CheckoutUiState(
                        mensagem = mensagem
                    )
                }

            } catch (e: CancellationException) {
                throw e
            } catch (e: IOException) {
                _checkoutState.value = CheckoutUiState(
                    mensagem = "Não foi possível conectar ao servidor."
                )
            } catch (e: Exception) {
                _checkoutState.value = CheckoutUiState(
                    mensagem = "Ocorreu um erro ao criar o pedido."
                )
            } finally {
                _checkoutState.value = _checkoutState.value.copy(
                    carregando = false
                )
            }
        }
    }

    fun limparResultadoCheckout() {
        _checkoutState.value = CheckoutUiState()
    }

    private fun monitorarPagamento(
        pedidoId: Long,
        token: String,
        itensDoPedido: List<ItemCarrinho>
    ) {
        monitoramentoPagamentoJob?.cancel()

        monitoramentoPagamentoJob = viewModelScope.launch {
            repeat(200) {
                delay(3_000)

                try {
                    val resposta = RetrofitClient.pedidoApi.buscarPedido(
                        token = "Bearer $token",
                        pedidoId = pedidoId
                    )

                    if (resposta.code() in listOf(401, 403, 404)) {
                        _checkoutState.value = CheckoutUiState(
                            mensagem = "Não foi possível consultar o pedido.",
                            pedidoId = pedidoId
                        )
                        return@launch
                    }

                    if (resposta.isSuccessful) {
                        val pedido = resposta.body()

                        if (pedido?.status == "PAGO") {

                            // Só limpa se o carrinho não foi alterado
                            // enquanto o pagamento estava em andamento.
                            if (_itens.value == itensDoPedido) {
                                limparCarrinho()
                            }

                            _checkoutState.value = CheckoutUiState(
                                mensagem = "Pagamento aprovado! Pedido confirmado.",
                                pedidoId = pedidoId
                            )

                            return@launch
                        }
                    }

                } catch (e: CancellationException) {
                    throw e
                } catch (e: Exception) {
                    // Falhas temporárias serão tentadas novamente.
                }
            }

            _checkoutState.value = CheckoutUiState(
                mensagem = "Ainda não recebemos a confirmação do pagamento. Consulte seus pedidos novamente.",
                pedidoId = pedidoId
            )
        }
    }
}
