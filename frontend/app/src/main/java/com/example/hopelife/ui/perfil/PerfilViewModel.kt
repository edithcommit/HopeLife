package com.example.hopelife.ui.perfil

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.hopelife.data.api.RetrofitClient
import com.example.hopelife.data.dto.PerfilUpdateRequest
import com.example.hopelife.data.repository.AuthRepository
import com.example.hopelife.data.session.SessaoUsuario
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

data class PerfilUiState(
    val nome: String = "",
    val endereco: String = "",
    val editando: Boolean = false,
    val carregando: Boolean = false,
    val mensagem: String? = null,
    val erro: Boolean = false
)

class PerfilViewModel(
    private val repository: AuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(
        PerfilUiState(
            nome = SessaoUsuario.obterUsuario()?.nome.orEmpty(),
            endereco = SessaoUsuario.obterUsuario()?.endereco.orEmpty()
        )
    )

    val uiState: StateFlow<PerfilUiState> = _uiState.asStateFlow()

    fun alterarNome(valor: String) {
        _uiState.update { it.copy(nome = valor, mensagem = null) }
    }

    fun alterarEndereco(valor: String) {
        _uiState.update { it.copy(endereco = valor, mensagem = null) }
    }

    fun iniciarEdicao() {
        val usuario = SessaoUsuario.obterUsuario() ?: return

        _uiState.update {
            it.copy(
                nome = usuario.nome,
                endereco = usuario.endereco.orEmpty(),
                editando = true,
                mensagem = null,
                erro = false
            )
        }
    }

    fun cancelarEdicao() {
        val usuario = SessaoUsuario.obterUsuario()

        _uiState.update {
            it.copy(
                nome = usuario?.nome.orEmpty(),
                endereco = usuario?.endereco.orEmpty(),
                editando = false,
                mensagem = null,
                erro = false
            )
        }
    }

    fun salvarPerfil() {
        val atual = _uiState.value
        val usuario = SessaoUsuario.obterUsuario()

        if (usuario == null) {
            mostrarErro("Sua sessão terminou. Entre novamente.")
            return
        }

        val nome = atual.nome.trim()
        val endereco = atual.endereco.trim()

        if (nome.isBlank()) {
            mostrarErro("Informe seu nome.")
            return
        }

        if (nome.length > 100) {
            mostrarErro("O nome deve ter no máximo 100 caracteres.")
            return
        }

        if (endereco.length > 255) {
            mostrarErro("O endereço deve ter no máximo 255 caracteres.")
            return
        }

        val token = usuario.token
        if (token.isBlank()) {
            mostrarErro("Token de autenticação não encontrado. Entre novamente.")
            return
        }

        viewModelScope.launch {
            _uiState.update {
                it.copy(carregando = true, mensagem = null, erro = false)
            }

            try {
                val resposta = repository.atualizarPerfil(
                    token = token,
                    dados = PerfilUpdateRequest(
                        nome = nome,
                        endereco = endereco.ifBlank { null },
                        senha = null
                    )
                )

                // Atualiza os dados locais, preservando o token e o restante
                // da sessão já existente.
                SessaoUsuario.salvarUsuario(
                    usuario.copy(
                        nome = resposta.nome,
                        endereco = resposta.endereco
                    )
                )

                _uiState.update {
                    it.copy(
                        nome = resposta.nome,
                        endereco = resposta.endereco.orEmpty(),
                        editando = false,
                        carregando = false,
                        mensagem = "Perfil atualizado com sucesso!",
                        erro = false
                    )
                }
            } catch (e: HttpException) {
                val mensagem = when (e.code()) {
                    401 -> "Sua sessão expirou. Entre novamente."
                    403 -> "Você não tem permissão para alterar este perfil."
                    400 -> "Confira os dados informados e tente novamente."
                    else -> "Não foi possível atualizar o perfil. Tente novamente."
                }
                mostrarErro(mensagem)
            } catch (e: IOException) {
                mostrarErro("Não foi possível conectar ao servidor.")
            } catch (e: Exception) {
                mostrarErro("Ocorreu um erro inesperado ao atualizar o perfil.")
            } finally {
                _uiState.update { it.copy(carregando = false) }
            }
        }
    }

    private fun mostrarErro(mensagem: String) {
        _uiState.update {
            it.copy(mensagem = mensagem, erro = true, carregando = false)
        }
    }
}

class PerfilViewModelFactory : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PerfilViewModel::class.java)) {
            return PerfilViewModel(
                AuthRepository(RetrofitClient.authApi)
            ) as T
        }
        throw IllegalArgumentException("ViewModel desconhecido")
    }
}