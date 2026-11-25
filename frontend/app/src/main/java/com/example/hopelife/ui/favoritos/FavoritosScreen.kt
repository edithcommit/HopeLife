
package com.example.hopelife.ui.favoritos

import android.widget.Toast
import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember

import com.example.hopelife.ui.home.ProdutoViewModel
import com.example.hopelife.ui.carrinho.CarrinhoViewModel
import com.example.hopelife.ui.theme.DeepLilac
import com.example.hopelife.ui.theme.DeepPink
import com.example.hopelife.ui.theme.HopeBackground
import com.example.hopelife.ui.theme.HopeTextSecondary
import com.example.hopelife.ui.theme.LightLilac

@Composable
fun FavoritosScreen(
    viewModel: ProdutoViewModel,
    carrinhoViewModel: CarrinhoViewModel
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.carregarFavoritos()
    }

    LaunchedEffect(uiState.mensagemFavorito) {
        uiState.mensagemFavorito?.let { mensagem ->
            Toast.makeText(context, mensagem, Toast.LENGTH_SHORT).show()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(HopeBackground)
            .padding(horizontal = 22.dp)
    ) {
        Text(
            text = "Meus favoritos ♡",
            modifier = Modifier.padding(top = 24.dp),
            style = MaterialTheme.typography.headlineMedium,
            color = DeepLilac
        )

        Text(
            text = "Seus produtos favoritos estão aqui.",
            modifier = Modifier.padding(top = 6.dp, bottom = 20.dp),
            style = MaterialTheme.typography.bodyMedium,
            color = HopeTextSecondary
        )

        when {
            uiState.carregando && uiState.favoritos.isEmpty() -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        "Carregando favoritos...",
                        color = HopeTextSecondary
                    )
                }
            }

            uiState.favoritos.isEmpty() -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.FavoriteBorder,
                            contentDescription = null,
                            tint = DeepPink,
                            modifier = Modifier.size(54.dp)
                        )

                        Spacer(Modifier.height(12.dp))

                        Text(
                            "Você ainda não tem favoritos.",
                            style = MaterialTheme.typography.titleMedium,
                            color = DeepLilac
                        )

                        Text(
                            "Toque no coração dos produtos para salvá-los aqui.",
                            style = MaterialTheme.typography.bodySmall,
                            color = HopeTextSecondary
                        )
                    }
                }
            }

            else -> {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    modifier = Modifier.fillMaxSize(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(
                        items = uiState.favoritos,
                        key = { it.id }
                    ) { produto ->

                        Card(
                            shape = RoundedCornerShape(21.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = Color.White
                            ),
                            elevation = CardDefaults.cardElevation(
                                defaultElevation = 1.dp
                            )
                        ) {
                            Column(
                                modifier = Modifier.padding(12.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(100.dp)
                                        .clip(RoundedCornerShape(15.dp))
                                        .background(LightLilac),
                                    contentAlignment = Alignment.Center
                                ) {
                                    if (produto.imagemUrl.isNullOrBlank()) {
                                        Text(
                                            "🛍️",
                                            style = MaterialTheme.typography.headlineLarge
                                        )
                                    } else {
                                        AsyncImage(
                                            model = produto.imagemUrl,
                                            contentDescription = produto.nome,
                                            contentScale = ContentScale.Crop,
                                            modifier = Modifier.fillMaxSize()
                                        )
                                    }

                                    Surface(
                                        modifier = Modifier
                                            .align(Alignment.TopEnd)
                                            .padding(7.dp)
                                            .size(36.dp),
                                        shape = CircleShape,
                                        color = Color.White.copy(alpha = 0.95f),
                                        shadowElevation = 3.dp
                                    ) {
                                        IconButton(
                                            onClick = {
                                                viewModel.alternarFavorito(produto)
                                            },
                                            enabled = produto.id !in uiState.operacoesFavorito
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.Favorite,
                                                contentDescription = "Remover dos favoritos",
                                                tint = DeepPink,
                                                modifier = Modifier.size(21.dp)
                                            )
                                        }
                                    }
                                }

                                Spacer(Modifier.height(12.dp))

                                Text(
                                    produto.nome,
                                    style = MaterialTheme.typography.titleMedium,
                                    color = DeepLilac
                                )

                                Spacer(Modifier.height(8.dp))

                                Button(
                                    onClick = {
                                        val adicionado =
                                            carrinhoViewModel.adicionarProduto(produto)

                                        val mensagem = if (adicionado) {
                                            "${produto.nome} adicionado ao carrinho!"
                                        } else {
                                            "Estoque máximo atingido ou produto indisponível."
                                        }

                                        Toast.makeText(
                                            context,
                                            mensagem,
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    },
                                    modifier = Modifier.fillMaxWidth(),
                                    enabled = produto.quantidadeEstoque > 0
                                ) {
                                    Text(
                                        if (produto.quantidadeEstoque > 0) {
                                            "Adicionar ao carrinho"
                                        } else {
                                            "Indisponível"
                                        }
                                    )
                                }

                                if (produto.exigeReceita) {
                                    Spacer(Modifier.height(8.dp))
                                    Text(
                                        "⚕ Receita médica obrigatória",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = Color(0xFFB3261E)
                                    )
                                }

                                Spacer(Modifier.height(4.dp))

                                Text(
                                    produto.descricao ?: "Sem descrição disponível",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = HopeTextSecondary
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
