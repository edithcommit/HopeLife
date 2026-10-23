package com.example.hopelife.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hopelife.data.api.RetrofitClient
import com.example.hopelife.data.dto.LoginRequest
import com.example.hopelife.data.session.SessaoUsuario
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.io.IOException

data class LoginUiState(
    val carregando: Boolean = false,
    val mensagem: String? = null,
    val sucesso: Boolean = false,
    val perfilAutenticado: String? = null
)

class LoginViewModel : ViewModel() {

    private val api = RetrofitClient.authApi

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState = _uiState.asStateFlow()

    fun login(email: String, senha: String, perfil: String) {
        if (email.isBlank() || senha.isBlank()) {
            _uiState.value = LoginUiState(
                mensagem = "Preencha o e-mail e a senha."
            )
            return
        }

        viewModelScope.launch {
            _uiState.value = LoginUiState(carregando = true)

            try {
                val dadosLogin = LoginRequest(
                    email = email.trim(),
                    senha = senha
                )

                val resposta = when (perfil) {
                    "FARMACEUTICO" -> api.loginFarmaceutico(dadosLogin)
                    else -> api.login(dadosLogin)
                }

                val dados = resposta.body()

                if (resposta.isSuccessful && dados != null) {
                    if (dados.token.isBlank()) {
                        _uiState.value = LoginUiState(
                            mensagem = "O servidor não retornou um token válido."
                        )
                        return@launch
                    }

                    if (!dados.perfil.equals(perfil, ignoreCase = true)) {
                        _uiState.value = LoginUiState(
                            mensagem = "Esta conta não pertence ao perfil selecionado."
                        )
                        return@launch
                    }

                    SessaoUsuario.salvarUsuario(dados)

                    _uiState.value = LoginUiState(
                        mensagem = "Login realizado com sucesso!",
                        sucesso = true,
                        perfilAutenticado = dados.perfil.uppercase()
                    )
                } else {
                    _uiState.value = LoginUiState(
                        mensagem = when (resposta.code()) {
                            401 -> "E-mail ou senha inválidos, ou perfil não autorizado."
                            403 -> "Você não tem permissão para acessar este perfil."
                            else -> "Não foi possível entrar. Código: ${resposta.code()}."
                        }
                    )
                }
            } catch (e: CancellationException) {
                throw e
            } catch (e: IOException) {
                _uiState.value = LoginUiState(
                    mensagem = "Não foi possível conectar ao servidor."
                )
            } catch (e: Exception) {
                _uiState.value = LoginUiState(
                    mensagem = "Ocorreu um erro ao realizar o login."
                )
            }
        }
    }
}