
package com.example.hopelife.ui.perfil

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.hopelife.data.session.SessaoUsuario
import com.example.hopelife.ui.theme.DeepLilac
import com.example.hopelife.ui.theme.HopeBackground
import com.example.hopelife.ui.theme.HopeTextSecondary
import com.example.hopelife.ui.theme.LightLilac

private val PerfilTextDark = Color(0xFF30283B)
private val PerfilTextMedium = Color(0xFF62596D)
private val PerfilWhite = Color(0xFFFFFFFF)
private val PerfilBorder = Color(0xFFE7DFF0)
private val PerfilDanger = Color(0xFFB94C5A)

@Composable
fun PerfilScreen(
    onLogout: () -> Unit,
    perfilViewModel: PerfilViewModel = viewModel(
        factory = PerfilViewModelFactory()
    )
) {
    val estado by perfilViewModel.uiState.collectAsStateWithLifecycle()
    val usuario = SessaoUsuario.obterUsuario()

    var mostrarDialogoExclusao by remember {
        mutableStateOf(false)
    }

    LaunchedEffect(estado.contaExcluida) {
        if (estado.contaExcluida) {
            onLogout()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(HopeBackground)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 22.dp, vertical = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        // CABEÇALHO

        Text(
            text = "Meu perfil",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = DeepLilac
        )

        Spacer(modifier = Modifier.height(28.dp))

        if (usuario == null) {

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(
                    containerColor = PerfilWhite
                ),
                elevation = CardDefaults.cardElevation(2.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(28.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = null,
                        tint = DeepLilac,
                        modifier = Modifier.size(48.dp)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "Sua sessão terminou",
                        fontWeight = FontWeight.Bold,
                        color = PerfilTextDark
                    )

                    Text(
                        text = "Entre novamente para acessar seu perfil.",
                        color = PerfilTextMedium
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    Button(
                        onClick = onLogout,
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = DeepLilac
                        ),
                        shape = RoundedCornerShape(14.dp)
                    ) {
                        Text("Voltar ao login")
                    }
                }
            }

        } else {

            // AVATAR

            Box(
                modifier = Modifier
                    .size(104.dp)
                    .clip(CircleShape)
                    .background(LightLilac),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = estado.nome.trim()
                        .firstOrNull()
                        ?.uppercase() ?: "?",
                    style = MaterialTheme.typography.displayMedium,
                    fontWeight = FontWeight.Bold,
                    color = DeepLilac
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            Text(
                text = estado.nome,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = PerfilTextDark
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = usuario.email,
                style = MaterialTheme.typography.bodyMedium,
                color = PerfilTextMedium
            )

            Spacer(modifier = Modifier.height(30.dp))

            // INFORMAÇÕES PESSOAIS

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(
                    containerColor = PerfilWhite
                ),
                elevation = CardDefaults.cardElevation(
                    defaultElevation = 2.dp
                )
            ) {
                Column(
                    modifier = Modifier.padding(22.dp)
                ) {

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "Informações pessoais",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = DeepLilac
                            )

                            Spacer(modifier = Modifier.height(3.dp))

                            Text(
                                text = "Seus dados cadastrados",
                                style = MaterialTheme.typography.bodySmall,
                                color = PerfilTextMedium
                            )
                        }

                        if (!estado.editando) {
                            IconButton(
                                onClick = perfilViewModel::iniciarEdicao
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Edit,
                                    contentDescription = "Editar perfil",
                                    tint = DeepLilac
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(22.dp))

                    if (estado.editando) {

                        // CAMPO NOME

                        OutlinedTextField(
                            value = estado.nome,
                            onValueChange = perfilViewModel::alterarNome,
                            label = {
                                Text("Nome completo")
                            },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth(),
                            enabled = !estado.carregando,
                            shape = RoundedCornerShape(14.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedTextColor = PerfilTextDark,
                                unfocusedTextColor = PerfilTextDark,
                                disabledTextColor = PerfilTextMedium,
                                focusedLabelColor = DeepLilac,
                                unfocusedLabelColor = PerfilTextMedium,
                                focusedBorderColor = DeepLilac,
                                unfocusedBorderColor = PerfilBorder,
                                cursorColor = DeepLilac,
                                focusedContainerColor = PerfilWhite,
                                unfocusedContainerColor = PerfilWhite
                            )
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        // CAMPO ENDEREÇO

                        OutlinedTextField(
                            value = estado.endereco,
                            onValueChange = perfilViewModel::alterarEndereco,
                            label = {
                                Text("Endereço")
                            },
                            placeholder = {
                                Text(
                                    "Digite seu endereço (opcional)",
                                    color = PerfilTextMedium
                                )
                            },
                            modifier = Modifier.fillMaxWidth(),
                            enabled = !estado.carregando,
                            minLines = 2,
                            maxLines = 3,
                            shape = RoundedCornerShape(14.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedTextColor = PerfilTextDark,
                                unfocusedTextColor = PerfilTextDark,
                                disabledTextColor = PerfilTextMedium,
                                focusedLabelColor = DeepLilac,
                                unfocusedLabelColor = PerfilTextMedium,
                                focusedBorderColor = DeepLilac,
                                unfocusedBorderColor = PerfilBorder,
                                cursorColor = DeepLilac,
                                focusedContainerColor = PerfilWhite,
                                unfocusedContainerColor = PerfilWhite
                            )
                        )

                        Spacer(modifier = Modifier.height(22.dp))

                        // SALVAR

                        Button(
                            onClick = perfilViewModel::salvarPerfil,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(52.dp),
                            enabled = !estado.carregando,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = DeepLilac
                            ),
                            shape = RoundedCornerShape(14.dp)
                        ) {
                            if (estado.carregando) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(20.dp),
                                    color = Color.White,
                                    strokeWidth = 2.dp
                                )

                                Spacer(modifier = Modifier.width(10.dp))

                                Text("Salvando...")
                            } else {
                                Text(
                                    text = "Salvar alterações",
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        // CANCELAR

                        OutlinedButton(
                            onClick = perfilViewModel::cancelarEdicao,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(48.dp),
                            enabled = !estado.carregando,
                            shape = RoundedCornerShape(14.dp),
                            colors = ButtonDefaults.outlinedButtonColors(
                                contentColor = DeepLilac
                            )
                        ) {
                            Text("Cancelar")
                        }

                    } else {

                        // VISUALIZAÇÃO DOS DADOS

                        LinhaPerfil(
                            icone = {
                                Icon(
                                    imageVector = Icons.Default.Person,
                                    contentDescription = null,
                                    tint = DeepLilac
                                )
                            },
                            titulo = "Nome completo",
                            valor = estado.nome
                        )

                        Spacer(modifier = Modifier.height(22.dp))

                        HorizontalDivider(
                            color = PerfilBorder
                        )

                        Spacer(modifier = Modifier.height(22.dp))

                        LinhaPerfil(
                            icone = {
                                Icon(
                                    imageVector = Icons.Default.LocationOn,
                                    contentDescription = null,
                                    tint = DeepLilac
                                )
                            },
                            titulo = "Endereço",
                            valor = estado.endereco
                                .takeIf { it.isNotBlank() }
                                ?: "Endereço não informado"
                        )
                    }

                    // MENSAGENS

                    estado.mensagem?.let { mensagem ->

                        Spacer(modifier = Modifier.height(16.dp))

                        Text(
                            text = mensagem,
                            color = if (estado.erro) {
                                MaterialTheme.colorScheme.error
                            } else {
                                DeepLilac
                            },
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(28.dp))

            // AÇÕES DA CONTA

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(
                    containerColor = PerfilWhite
                ),
                elevation = CardDefaults.cardElevation(2.dp)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp)
                ) {
                    Text(
                        text = "Conta",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = DeepLilac
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = "Gerencie o acesso à sua conta.",
                        style = MaterialTheme.typography.bodySmall,
                        color = PerfilTextMedium
                    )

                    Spacer(modifier = Modifier.height(18.dp))

                    // SAIR DA CONTA

                    OutlinedButton(
                        onClick = {
                            SessaoUsuario.limpar()
                            onLogout()
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp),
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = DeepLilac
                        ),
                        border = androidx.compose.foundation.BorderStroke(
                            1.dp,
                            PerfilBorder
                        )
                    ) {
                        Icon(
                            imageVector = Icons.Default.Logout,
                            contentDescription = null
                        )

                        Spacer(modifier = Modifier.width(10.dp))

                        Text(
                            text = "Sair da conta",
                            fontWeight = FontWeight.SemiBold
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // EXCLUIR CONTA

                    OutlinedButton(
                        onClick = {
                            mostrarDialogoExclusao = true
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp),
                        enabled = !estado.excluindo,
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = PerfilDanger
                        ),
                        border = androidx.compose.foundation.BorderStroke(
                            1.dp,
                            Color(0xFFE9C9CD)
                        )
                    ) {
                        Text(
                            text = "Excluir conta",
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Feito com carinho 💜 HopeLife",
                style = MaterialTheme.typography.labelMedium,
                color = HopeTextSecondary
            )

            Spacer(modifier = Modifier.height(16.dp))

            // DIÁLOGO DE EXCLUSÃO

            if (mostrarDialogoExclusao) {
                AlertDialog(
                    onDismissRequest = {
                        if (!estado.excluindo) {
                            mostrarDialogoExclusao = false
                        }
                    },
                    containerColor = PerfilWhite,
                    title = {
                        Text(
                            text = "Excluir conta",
                            color = PerfilTextDark,
                            fontWeight = FontWeight.Bold
                        )
                    },
                    text = {
                        Text(
                            text = "Tem certeza de que deseja excluir sua conta? " +
                                    "Essa ação é permanente e não poderá ser desfeita.",
                            color = PerfilTextMedium
                        )
                    },
                    confirmButton = {
                        TextButton(
                            onClick = {
                                perfilViewModel.excluirConta()
                            },
                            enabled = !estado.excluindo
                        ) {
                            if (estado.excluindo) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(18.dp),
                                    strokeWidth = 2.dp,
                                    color = PerfilDanger
                                )
                            } else {
                                Text(
                                    text = "Sim, excluir",
                                    color = PerfilDanger
                                )
                            }
                        }
                    },
                    dismissButton = {
                        TextButton(
                            onClick = {
                                mostrarDialogoExclusao = false
                            },
                            enabled = !estado.excluindo
                        ) {
                            Text(
                                text = "Cancelar",
                                color = DeepLilac
                            )
                        }
                    }
                )
            }
        }
    }
}

@Composable
private fun LinhaPerfil(
    icone: @Composable () -> Unit,
    titulo: String,
    valor: String
) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(RoundedCornerShape(15.dp))
                .background(LightLilac),
            contentAlignment = Alignment.Center
        ) {
            icone()
        }

        Spacer(modifier = Modifier.width(14.dp))

        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = titulo,
                style = MaterialTheme.typography.labelMedium,
                color = PerfilTextMedium
            )

            Spacer(modifier = Modifier.height(5.dp))

            Text(
                text = valor,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.SemiBold,
                color = PerfilTextDark
            )
        }
    }
}
