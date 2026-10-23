
package com.example.hopelife.ui.cadastro

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.MedicalServices
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.hopelife.ui.theme.DeepLilac
import com.example.hopelife.ui.theme.DeepPink
import com.example.hopelife.ui.theme.HopeBackground
import com.example.hopelife.ui.theme.HopeOutline
import com.example.hopelife.ui.theme.HopeTextSecondary
import com.example.hopelife.ui.theme.LightLilac
import com.example.hopelife.ui.theme.LightPink

@Composable
fun CadastroScreen(
    factory: CadastroViewModelFactory,
    onIrParaLogin: () -> Unit
) {
    val vm: CadastroViewModel = viewModel(factory = factory)
    val state by vm.uiState.collectAsState()

    var nome by rememberSaveable { mutableStateOf("") }
    var email by rememberSaveable { mutableStateOf("") }
    var senha by rememberSaveable { mutableStateOf("") }
    var endereco by rememberSaveable { mutableStateOf("") }
    var matricula by rememberSaveable { mutableStateOf("") }
    var tipoConta by rememberSaveable { mutableStateOf("CLIENTE") }
    var mostrarSenha by rememberSaveable { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(HopeBackground)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 24.dp, vertical = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(Modifier.height(8.dp))

        // Cabeçalho da marca
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(LightPink),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Favorite,
                    contentDescription = null,
                    tint = DeepPink,
                    modifier = Modifier.size(30.dp)
                )
                Icon(
                    imageVector = Icons.Default.MedicalServices,
                    contentDescription = "Símbolo de saúde",
                    tint = Color.White,
                    modifier = Modifier.size(16.dp)
                )
            }

            Spacer(Modifier.width(10.dp))

            Column {
                Text(
                    text = "HopeLife",
                    style = MaterialTheme.typography.headlineMedium,
                    color = DeepLilac
                )
                Text(
                    text = "Mais saúde para o seu dia",
                    style = MaterialTheme.typography.bodySmall,
                    color = HopeTextSecondary
                )
            }
        }

        Spacer(Modifier.height(26.dp))

        Text(
            text = "Vamos começar? ♡",
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.onBackground
        )

        Spacer(Modifier.height(6.dp))

        Text(
            text = "Crie sua conta para fazer parte da HopeLife.",
            style = MaterialTheme.typography.bodyMedium,
            color = HopeTextSecondary
        )

        Spacer(Modifier.height(24.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(26.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color.White
            ),
            elevation = CardDefaults.cardElevation(
                defaultElevation = 3.dp
            )
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                Text(
                    text = "Criar conta",
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Text(
                    text = "Qual opção descreve você?",
                    style = MaterialTheme.typography.bodyMedium,
                    color = HopeTextSecondary
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    TipoContaCard(
                        titulo = "Cliente",
                        descricao = "Comprar produtos",
                        selecionado = tipoConta == "CLIENTE",
                        iconeCliente = true,
                        modifier = Modifier.weight(1f),
                        enabled = !state.carregando && !state.sucesso
                    ) {
                        tipoConta = "CLIENTE"
                        vm.limparMensagem()
                    }

                    TipoContaCard(
                        titulo = "Farmacêutico",
                        descricao = "Acesso profissional",
                        selecionado = tipoConta == "FARMACEUTICO",
                        iconeCliente = false,
                        modifier = Modifier.weight(1f),
                        enabled = !state.carregando && !state.sucesso
                    ) {
                        tipoConta = "FARMACEUTICO"
                        vm.limparMensagem()
                    }
                }

                Surface(
                    color = LightLilac,
                    shape = RoundedCornerShape(14.dp)
                ) {
                    Text(
                        text = if (tipoConta == "CLIENTE") {
                            "Cadastre-se para explorar nossos produtos e cuidados."
                        } else {
                            "Tenha sua matrícula profissional em mãos para se cadastrar."
                        },
                        modifier = Modifier.padding(12.dp),
                        style = MaterialTheme.typography.bodyMedium,
                        color = DeepLilac
                    )
                }

                OutlinedTextField(
                    value = nome,
                    onValueChange = {
                        nome = it
                        vm.limparMensagem()
                    },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = !state.carregando && !state.sucesso,
                    singleLine = true,
                    shape = RoundedCornerShape(15.dp),
                    label = { Text("Nome completo *") },
                    placeholder = { Text("Ex.: Maria Silva") },
                    leadingIcon = {
                        Icon(Icons.Default.Person, contentDescription = null)
                    },
                    keyboardOptions = KeyboardOptions(
                        capitalization = androidx.compose.ui.text.input.KeyboardCapitalization.Words
                    ),
                    colors = camposCadastro()
                )

                OutlinedTextField(
                    value = email,
                    onValueChange = {
                        email = it
                        vm.limparMensagem()
                    },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = !state.carregando && !state.sucesso,
                    singleLine = true,
                    shape = RoundedCornerShape(15.dp),
                    label = { Text("E-mail *") },
                    placeholder = { Text("voce@exemplo.com") },
                    leadingIcon = {
                        Icon(Icons.Default.Email, contentDescription = null)
                    },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Email
                    ),
                    colors = camposCadastro()
                )

                OutlinedTextField(
                    value = senha,
                    onValueChange = {
                        senha = it
                        vm.limparMensagem()
                    },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = !state.carregando && !state.sucesso,
                    singleLine = true,
                    shape = RoundedCornerShape(15.dp),
                    label = { Text("Senha *") },
                    placeholder = { Text("Mínimo de 8 caracteres") },
                    leadingIcon = {
                        Icon(Icons.Default.Lock, contentDescription = null)
                    },
                    visualTransformation = if (mostrarSenha) {
                        VisualTransformation.None
                    } else {
                        PasswordVisualTransformation()
                    },
                    trailingIcon = {
                        IconButton(
                            onClick = { mostrarSenha = !mostrarSenha }
                        ) {
                            Icon(
                                imageVector = if (mostrarSenha) {
                                    Icons.Default.VisibilityOff
                                } else {
                                    Icons.Default.Visibility
                                },
                                contentDescription = if (mostrarSenha) {
                                    "Ocultar senha"
                                } else {
                                    "Mostrar senha"
                                }
                            )
                        }
                    },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Password
                    ),
                    colors = camposCadastro()
                )

                if (tipoConta == "CLIENTE") {
                    OutlinedTextField(
                        value = endereco,
                        onValueChange = {
                            endereco = it
                            vm.limparMensagem()
                        },
                        modifier = Modifier.fillMaxWidth(),
                        enabled = !state.carregando && !state.sucesso,
                        singleLine = true,
                        shape = RoundedCornerShape(15.dp),
                        label = { Text("Endereço (opcional)") },
                        placeholder = { Text("Rua, número, bairro") },
                        leadingIcon = {
                            Icon(
                                Icons.Default.LocationOn,
                                contentDescription = null
                            )
                        },
                        colors = camposCadastro()
                    )
                } else {
                    OutlinedTextField(
                        value = matricula,
                        onValueChange = {
                            matricula = it
                            vm.limparMensagem()
                        },
                        modifier = Modifier.fillMaxWidth(),
                        enabled = !state.carregando && !state.sucesso,
                        singleLine = true,
                        shape = RoundedCornerShape(15.dp),
                        label = { Text("Matrícula profissional *") },
                        placeholder = { Text("Ex.: 1-12345") },
                        leadingIcon = {
                            Icon(
                                Icons.Default.MedicalServices,
                                contentDescription = null
                            )
                        },
                        colors = camposCadastro()
                    )
                }

                Button(
                    onClick = {
                        vm.cadastrar(
                            nome = nome,
                            email = email,
                            senha = senha,
                            endereco = endereco,
                            tipoConta = tipoConta,
                            matricula = matricula
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(54.dp),
                    enabled = !state.carregando && !state.sucesso,
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = DeepLilac,
                        contentColor = Color.White
                    )
                ) {
                    if (state.carregando) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(22.dp),
                            color = Color.White,
                            strokeWidth = 2.dp
                        )
                    } else {
                        Text(
                            text = "Criar minha conta",
                            style = MaterialTheme.typography.titleMedium
                        )
                    }
                }

                state.mensagem?.let { mensagem ->
                    Surface(
                        color = if (state.sucesso) {
                            LightLilac
                        } else {
                            MaterialTheme.colorScheme.errorContainer
                        },
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(
                            text = mensagem,
                            modifier = Modifier.padding(12.dp),
                            color = if (state.sucesso) {
                                DeepLilac
                            } else {
                                MaterialTheme.colorScheme.onErrorContainer
                            },
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }

                if (state.sucesso) {
                    Button(
                        onClick = onIrParaLogin,
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(15.dp)
                    ) {
                        Text("Ir para o login")
                    }
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Já tem uma conta?",
                        color = HopeTextSecondary,
                        style = MaterialTheme.typography.bodyMedium
                    )
                    TextButton(
                        onClick = onIrParaLogin,
                        enabled = !state.carregando
                    ) {
                        Text(
                            text = "Entrar",
                            color = DeepPink,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }

        Spacer(Modifier.height(20.dp))

        Text(
            text = "Cuidar de você é a nossa prioridade. ♡",
            style = MaterialTheme.typography.bodyMedium,
            color = HopeTextSecondary
        )

        Spacer(Modifier.height(12.dp))
    }
}

@Composable
private fun TipoContaCard(
    titulo: String,
    descricao: String,
    selecionado: Boolean,
    iconeCliente: Boolean,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    onClick: () -> Unit
) {
    val corBorda = if (selecionado) DeepLilac else HopeOutline
    val corFundo = if (selecionado) LightLilac else Color.White

    Surface(
        modifier = modifier
            .border(
                width = if (selecionado) 2.dp else 1.dp,
                color = corBorda,
                shape = RoundedCornerShape(17.dp)
            )
            .clip(RoundedCornerShape(17.dp))
            .clickable(
                enabled = enabled,
                onClick = onClick
            ),
        color = corFundo,
        shape = RoundedCornerShape(17.dp)
    ) {
        Column(
            modifier = Modifier.padding(
                horizontal = 8.dp,
                vertical = 15.dp
            ),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Icon(
                imageVector = if (iconeCliente) {
                    Icons.Default.Person
                } else {
                    Icons.Default.MedicalServices
                },
                contentDescription = null,
                tint = if (selecionado) DeepLilac else HopeTextSecondary,
                modifier = Modifier.size(27.dp)
            )

            Text(
                text = titulo,
                style = MaterialTheme.typography.labelLarge,
                color = if (selecionado) DeepLilac else MaterialTheme.colorScheme.onSurface
            )

            Text(
                text = descricao,
                style = MaterialTheme.typography.labelSmall,
                color = HopeTextSecondary
            )
        }
    }
}

@Composable
private fun camposCadastro() = OutlinedTextFieldDefaults.colors(
    focusedBorderColor = DeepLilac,
    unfocusedBorderColor = HopeOutline,
    focusedLabelColor = DeepLilac,
    cursorColor = DeepLilac,
    focusedLeadingIconColor = DeepLilac,
    unfocusedLeadingIconColor = HopeTextSecondary,
    focusedTrailingIconColor = DeepLilac,
    unfocusedTrailingIconColor = HopeTextSecondary
)
