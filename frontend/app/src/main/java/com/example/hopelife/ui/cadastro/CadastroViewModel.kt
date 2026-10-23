
package com.example.hopelife.ui.cadastro

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hopelife.data.dto.CadastroFarmaceuticoRequest
import com.example.hopelife.data.dto.CadastroRequest
import com.example.hopelife.data.repository.AuthRepository
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException
import org.json.JSONObject

data class CadastroUiState(
    val carregando: Boolean = false,
    val mensagem: String? = null,
    val sucesso: Boolean = false
)

class CadastroViewModel(
    private val repository: AuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(CadastroUiState())
    val uiState = _uiState.asStateFlow()

    fun cadastrar(
        nome: String,
        email: String,
        senha: String,
        endereco: String,
        tipoConta: String,
        matricula: String
    ) {
        if (nome.isBlank() || email.isBlank() || senha.isBlank()) {
            _uiState.value = CadastroUiState(
                mensagem = "Preencha nome, e-mail e senha."
            )
            return
        }

        if (senha.length < 8) {
            _uiState.value = CadastroUiState(
                mensagem = "A senha deve ter pelo menos 8 caracteres."
            )
            return
        }

        if (tipoConta == "FARMACEUTICO" && matricula.isBlank()) {
            _uiState.value = CadastroUiState(
                mensagem = "Informe sua matrícula."
            )
            return
        }

        viewModelScope.launch {
            _uiState.value = CadastroUiState(carregando = true)

            try {
                val usuario = if (tipoConta == "FARMACEUTICO") {
                    repository.cadastrarFarmaceutico(
                        CadastroFarmaceuticoRequest(
                            nome = nome.trim(),
                            email = email.trim(),
                            senha = senha,
                            matricula = matricula.trim()
                        )
                    )
                } else {
                    repository.cadastrarCliente(
                        CadastroRequest(
                            nome = nome.trim(),
                            email = email.trim(),
                            senha = senha,
                            endereco = endereco.trim().takeIf {
                                it.isNotEmpty()
                            }
                        )
                    )
                }

                _uiState.value = CadastroUiState(
                    sucesso = true,
                    mensagem = "Cadastro realizado! Bem-vindo(a), ${usuario.nome}."
                )
            } catch (e: HttpException) {
                val corpoErro = e.response()
                    ?.errorBody()
                    ?.string()

                val mensagemServidor = try {
                    val json = JSONObject(corpoErro ?: "")
                    listOf("message", "mensagem", "detail", "erro")
                        .firstNotNullOfOrNull { chave ->
                            json.optString(chave)
                                .takeIf { it.isNotBlank() && it != "null" }
                        }
                } catch (ex: Exception) {
                    null
                }

                val mensagem = mensagemServidor ?: when (e.code()) {
                    400 -> "Dados inválidos. Confira os campos."
                    403 -> "Matrícula não autorizada para cadastro."
                    409 -> "Matrícula já utilizada ou e-mail já cadastrado."
                    else -> "Erro HTTP ${e.code()} ao cadastrar."
                }

                _uiState.value = CadastroUiState(
                    mensagem = mensagem
                )
            } catch (e: IOException) {
                _uiState.value = CadastroUiState(
                    mensagem = "Falha de conexão. Verifique o servidor e tente novamente."
                )
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                _uiState.value = CadastroUiState(
                    mensagem = e.message ?: "Ocorreu um erro inesperado."
                )
            }
        }
    }

    fun limparMensagem() {
        _uiState.value = _uiState.value.copy(mensagem = null)
    }
}
