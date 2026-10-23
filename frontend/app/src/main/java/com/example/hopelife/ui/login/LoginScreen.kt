
package com.example.hopelife.ui.login

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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material.icons.filled.MedicalServices
import androidx.compose.material.icons.filled.Person
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
import androidx.compose.runtime.LaunchedEffect
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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
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
fun LoginScreen(
    onIrParaCadastro: () -> Unit,
    onEntrarDemo: () -> Unit,
    onLoginSucesso: (String) -> Unit,
    loginViewModel: LoginViewModel = viewModel()
) {
    var email by rememberSaveable { mutableStateOf("") }
    var senha by rememberSaveable { mutableStateOf("") }
    var mostrarSenha by rememberSaveable { mutableStateOf(false) }

    var perfilSelecionado by rememberSaveable {
        mutableStateOf("CLIENTE")
    }

    val uiState by loginViewModel.uiState.collectAsState()

    LaunchedEffect(uiState.sucesso, uiState.perfilAutenticado) {
        if (uiState.sucesso) {
            uiState.perfilAutenticado?.let { perfil ->
                onLoginSucesso(perfil)
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(HopeBackground)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 24.dp, vertical = 28.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(Modifier.height(12.dp))

        // Marca HopeLife
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .size(54.dp)
                    .clip(RoundedCornerShape(18.dp))
                    .background(LightPink),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Favorite,
                    contentDescription = null,
                    tint = DeepPink,
                    modifier = Modifier.size(34.dp)
                )
                Icon(
                    imageVector = Icons.Default.MedicalServices,
                    contentDescription = "Símbolo de saúde",
                    tint = Color.White,
                    modifier = Modifier.size(17.dp)
                )
            }

            Spacer(Modifier.width(12.dp))

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

        Spacer(Modifier.height(32.dp))

        Text(
            text = "Que bom ter você aqui!",
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.onBackground
        )

        Spacer(Modifier.height(8.dp))

        Text(
            text = "Entre na sua conta para continuar.",
            style = MaterialTheme.typography.bodyLarge,
            color = HopeTextSecondary
        )

        Spacer(Modifier.height(28.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(28.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color.White
            ),
            elevation = CardDefaults.cardElevation(
                defaultElevation = 3.dp
            )
        ) {
            Column(
                modifier = Modifier.padding(22.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = "Entrar na conta",
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Text(
                    text = "Como você deseja acessar?",
                    style = MaterialTheme.typography.bodyMedium,
                    color = HopeTextSecondary
                )

                // Seleção de perfil
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    PerfilCard(
                        titulo = "Cliente",
                        selecionado = perfilSelecionado == "CLIENTE",
                        iconeCliente = true,
                        modifier = Modifier.weight(1f),
                        enabled = !uiState.carregando
                    ) {
                        perfilSelecionado = "CLIENTE"
                    }

                    PerfilCard(
                        titulo = "Farmacêutico",
                        selecionado = perfilSelecionado == "FARMACEUTICO",
                        iconeCliente = false,
                        modifier = Modifier.weight(1f),
                        enabled = !uiState.carregando
                    ) {
                        perfilSelecionado = "FARMACEUTICO"
                    }
                }

                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = !uiState.carregando,
                    singleLine = true,
                    shape = RoundedCornerShape(16.dp),
                    label = { Text("E-mail") },
                    placeholder = { Text("voce@exemplo.com") },
                    leadingIcon = {
                        Icon(
                            Icons.Default.Email,
                            contentDescription = null
                        )
                    },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Email
                    ),
                    colors = camposHopeLife()
                )

                OutlinedTextField(
                    value = senha,
                    onValueChange = { senha = it },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = !uiState.carregando,
                    singleLine = true,
                    shape = RoundedCornerShape(16.dp),
                    label = { Text("Senha") },
                    placeholder = { Text("Digite sua senha") },
                    leadingIcon = {
                        Icon(
                            Icons.Default.Lock,
                            contentDescription = null
                        )
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
                    colors = camposHopeLife()
                )

                Button(
                    onClick = {
                        loginViewModel.login(
                            email = email,
                            senha = senha,
                            perfil = perfilSelecionado
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(54.dp),
                    enabled = !uiState.carregando,
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = DeepLilac,
                        contentColor = Color.White
                    )
                ) {
                    if (uiState.carregando) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(23.dp),
                            color = Color.White,
                            strokeWidth = 2.dp
                        )
                    } else {
                        Text(
                            text = "Entrar",
                            style = MaterialTheme.typography.titleMedium
                        )
                    }
                }

                // Feedback do backend
                uiState.mensagem?.let { mensagem ->
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = if (uiState.sucesso) {
                            LightLilac
                        } else {
                            MaterialTheme.colorScheme.errorContainer
                        }
                    ) {
                        Text(
                            text = mensagem,
                            modifier = Modifier.padding(12.dp),
                            color = if (uiState.sucesso) {
                                DeepLilac
                            } else {
                                MaterialTheme.colorScheme.onErrorContainer
                            },
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }

                TextButton(
                    onClick = onEntrarDemo,
                    modifier = Modifier.fillMaxWidth(),
                    enabled = !uiState.carregando
                ) {
                    Text(
                        text = "▷  Explorar modo de demonstração",
                        color = DeepLilac
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Ainda não tem conta?",
                        color = HopeTextSecondary,
                        style = MaterialTheme.typography.bodyMedium
                    )
                    TextButton(
                        onClick = onIrParaCadastro,
                        enabled = !uiState.carregando
                    ) {
                        Text(
                            text = "Cadastre-se",
                            color = DeepPink,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }

        Spacer(Modifier.height(24.dp))

        Text(
            text = "Cuidar de você é a nossa prioridade. ♡",
            style = MaterialTheme.typography.bodyMedium,
            color = HopeTextSecondary
        )

        Spacer(Modifier.height(12.dp))
    }
}

@Composable
private fun PerfilCard(
    titulo: String,
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
            .height(94.dp)
            .border(
                width = if (selecionado) 2.dp else 1.dp,
                color = corBorda,
                shape = RoundedCornerShape(18.dp)
            )
            .clip(RoundedCornerShape(18.dp))
            .clickable(
                enabled = enabled,
                onClick = onClick
            ),
        color = corFundo,
        shape = RoundedCornerShape(18.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
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

            Spacer(Modifier.height(7.dp))

            Text(
                text = titulo,
                style = MaterialTheme.typography.labelLarge,
                color = if (selecionado) DeepLilac else HopeTextSecondary
            )
        }
    }
}

@Composable
private fun camposHopeLife() = OutlinedTextFieldDefaults.colors(
    focusedBorderColor = DeepLilac,
    unfocusedBorderColor = HopeOutline,
    focusedLabelColor = DeepLilac,
    cursorColor = DeepLilac,
    focusedLeadingIconColor = DeepLilac,
    unfocusedLeadingIconColor = HopeTextSecondary,
    focusedTrailingIconColor = DeepLilac,
    unfocusedTrailingIconColor = HopeTextSecondary
)
