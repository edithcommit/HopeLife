
package com.example.hopelife.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hopelife.data.api.RetrofitClient
import com.example.hopelife.data.dto.ProdutoResponse
import com.example.hopelife.data.repository.ProdutoRepository
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.io.IOException

data class ProdutoUiState(
    val carregando: Boolean = false,
    val produtos: List<ProdutoResponse> = emptyList(),
    val favoritos: List<ProdutoResponse> = emptyList(),
    val mensagemErro: String? = null,
    val mensagemFavorito: String? = null,
    val operacoesFavorito: Set<Long> = emptySet()
)

class ProdutoViewModel : ViewModel() {

    private val repository = ProdutoRepository(
        RetrofitClient.produtoApi
    )

    private val _uiState = MutableStateFlow(ProdutoUiState())
    val uiState = _uiState.asStateFlow()

    fun carregarProdutos() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                carregando = true,
                mensagemErro = null
            )

            try {
                val produtos = repository.listarProdutos()

                _uiState.value = _uiState.value.copy(
                    carregando = false,
                    produtos = produtos
                )

                carregarFavoritos()

            } catch (e: CancellationException) {
                throw e

            } catch (e: IOException) {
                _uiState.value = _uiState.value.copy(
                    carregando = false,
                    mensagemErro = "Não foi possível conectar ao servidor."
                )

            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    carregando = false,
                    mensagemErro = "Não foi possível carregar os produtos."
                )
            }
        }
    }

    fun carregarFavoritos() {
        viewModelScope.launch {
            try {
                val favoritos = repository.listarFavoritos()

                _uiState.value = _uiState.value.copy(
                    favoritos = favoritos
                )

            } catch (e: CancellationException) {
                throw e

            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    favoritos = emptyList()
                )
            }
        }
    }

    fun alternarFavorito(produto: ProdutoResponse) {
        val estadoAtual = _uiState.value

        if (produto.id in estadoAtual.operacoesFavorito) return

        val jaFavoritado = estadoAtual.favoritos.any {
            it.id == produto.id
        }

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                operacoesFavorito = estadoAtual.operacoesFavorito + produto.id,
                mensagemFavorito = null
            )

            try {
                if (jaFavoritado) {
                    repository.desfavoritar(produto.id)
                } else {
                    repository.favoritar(produto.id)
                }

                carregarFavoritos()

                _uiState.value = _uiState.value.copy(
                    mensagemFavorito = if (jaFavoritado) {
                        "Produto removido dos favoritos."
                    } else {
                        "Produto adicionado aos favoritos."
                    }
                )

            } catch (e: CancellationException) {
                throw e

            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    mensagemFavorito = "Não foi possível atualizar os favoritos."
                )

            } finally {
                _uiState.value = _uiState.value.copy(
                    operacoesFavorito = _uiState.value.operacoesFavorito - produto.id
                )
            }
        }
    }
}
