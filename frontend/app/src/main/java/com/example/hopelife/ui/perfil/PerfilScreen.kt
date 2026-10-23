package com.example.hopelife.ui.perfil

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.hopelife.data.session.SessaoUsuario
import com.example.hopelife.ui.theme.DeepLilac
import com.example.hopelife.ui.theme.HopeBackground
import com.example.hopelife.ui.theme.HopeTextSecondary
import com.example.hopelife.ui.theme.LightLilac

@Composable
fun PerfilScreen(
    onLogout: () -> Unit,
    perfilViewModel: PerfilViewModel = viewModel(
        factory = PerfilViewModelFactory()
    )
) {
    val estado by perfilViewModel.uiState.collectAsStateWithLifecycle()
    val usuario = SessaoUsuario.obterUsuario()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(HopeBackground)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 24.dp, vertical = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Meu perfil",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = DeepLilac
        )

        Spacer(Modifier.height(24.dp))

        if (usuario == null) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp)
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        Icons.Default.Person,
                        contentDescription = null,
                        tint = DeepLilac,
                        modifier = Modifier.size(48.dp)
                    )

                    Spacer(Modifier.height(12.dp))

                    Text("Sua sessão terminou")
                    Text(
                        "Entre novamente para acessar seu perfil.",
                        color = HopeTextSecondary
                    )

                    Spacer(Modifier.height(20.dp))

                    Button(
                        onClick = onLogout,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Voltar ao login")
                    }
                }
            }
        } else {
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .clip(CircleShape)
                    .background(LightLilac),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = estado.nome.trim().firstOrNull()
                        ?.uppercase() ?: "?",
                    style = MaterialTheme.typography.displayMedium,
                    fontWeight = FontWeight.Bold,
                    color = DeepLilac
                )
            }

            Spacer(Modifier.height(12.dp))

            Text(
                text = estado.nome,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = usuario.email,
                style = MaterialTheme.typography.bodyMedium,
                color = HopeTextSecondary
            )

            Spacer(Modifier.height(28.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                ),
                elevation = CardDefaults.cardElevation(
                    defaultElevation = 2.dp
                )
            ) {
                Column(Modifier.padding(20.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            "Informações pessoais",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = DeepLilac
                        )

                        if (!estado.editando) {
                            IconButton(onClick = perfilViewModel::iniciarEdicao) {
                                Icon(
                                    Icons.Default.Edit,
                                    contentDescription = "Editar perfil",
                                    tint = DeepLilac
                                )
                            }
                        }
                    }

                    Spacer(Modifier.height(16.dp))

                    if (estado.editando) {
                        OutlinedTextField(
                            value = estado.nome,
                            onValueChange = perfilViewModel::alterarNome,
                            label = { Text("Nome completo") },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth(),
                            enabled = !estado.carregando
                        )

                        Spacer(Modifier.height(12.dp))

                        OutlinedTextField(
                            value = estado.endereco,
                            onValueChange = perfilViewModel::alterarEndereco,
                            label = { Text("Endereço") },
                            placeholder = {
                                Text("Digite seu endereço (opcional)")
                            },
                            modifier = Modifier.fillMaxWidth(),
                            enabled = !estado.carregando,
                            minLines = 2,
                            maxLines = 3
                        )

                        Spacer(Modifier.height(20.dp))

                        Button(
                            onClick = perfilViewModel::salvarPerfil,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp),
                            enabled = !estado.carregando,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = DeepLilac
                            ),
                            shape = RoundedCornerShape(14.dp)
                        ) {
                            if (estado.carregando) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(20.dp),
                                    color = MaterialTheme.colorScheme.onPrimary,
                                    strokeWidth = 2.dp
                                )
                                Spacer(Modifier.width(10.dp))
                                Text("Salvando...")
                            } else {
                                Text("Salvar alterações")
                            }
                        }

                        Spacer(Modifier.height(8.dp))

                        OutlinedButton(
                            onClick = perfilViewModel::cancelarEdicao,
                            modifier = Modifier.fillMaxWidth(),
                            enabled = !estado.carregando
                        ) {
                            Text("Cancelar")
                        }
                    } else {
                        LinhaPerfil(
                            icone = {
                                Icon(
                                    Icons.Default.Person,
                                    contentDescription = null,
                                    tint = DeepLilac
                                )
                            },
                            titulo = "Nome completo",
                            valor = usuario.nome
                        )

                        Spacer(Modifier.height(20.dp))

                        LinhaPerfil(
                            icone = {
                                Icon(
                                    Icons.Default.LocationOn,
                                    contentDescription = null,
                                    tint = DeepLilac
                                )
                            },
                            titulo = "Endereço",
                            valor = usuario.endereco
                                ?.takeIf { it.isNotBlank() }
                                ?: "Endereço não informado"
                        )
                    }

                    estado.mensagem?.let { mensagem ->
                        Spacer(Modifier.height(12.dp))
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

            Spacer(Modifier.height(28.dp))

            OutlinedButton(
                onClick = {
                    SessaoUsuario.limpar()
                    onLogout()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp),
                shape = RoundedCornerShape(16.dp)
            ) {
                Icon(
                    Icons.Default.Logout,
                    contentDescription = null
                )
                Spacer(Modifier.width(10.dp))
                Text(
                    "Sair da conta",
                    fontWeight = FontWeight.SemiBold
                )
            }

            Spacer(Modifier.height(16.dp))

            Text(
                "Feito com carinho 💜 HopeLife",
                style = MaterialTheme.typography.labelMedium,
                color = HopeTextSecondary
            )
        }
    }
}

@Composable
private fun LinhaPerfil(
    icone: @Composable () -> Unit,
    titulo: String,
    valor: String
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier = Modifier
                .size(44.dp)
                .clip(RoundedCornerShape(14.dp))
                .background(LightLilac),
            contentAlignment = Alignment.Center
        ) {
            icone()
        }

        Column(
            modifier = Modifier
                .padding(start = 14.dp)
                .weight(1f)
        ) {
            Text(
                titulo,
                style = MaterialTheme.typography.labelMedium,
                color = HopeTextSecondary
            )

            Spacer(Modifier.height(4.dp))

            Text(
                valor,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Medium
            )
        }
    }
}
