
package com.example.hopelife.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Healing
import androidx.compose.material.icons.filled.MedicalServices
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material.icons.filled.Spa
import androidx.compose.material.icons.filled.Vaccines
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.hopelife.ui.theme.DeepLilac
import com.example.hopelife.ui.theme.DeepPink
import com.example.hopelife.ui.theme.HopeBackground
import com.example.hopelife.ui.theme.HopeOutline
import com.example.hopelife.ui.theme.HopeTextSecondary
import com.example.hopelife.ui.theme.LightLilac
import com.example.hopelife.ui.theme.LightPink

private data class CategoriaHome(
    val nome: String,
    val emoji: String
)

private data class ProdutoHome(
    val nome: String,
    val descricao: String,
    val emoji: String,
    val cor: Color
)

@Composable
fun HomeScreen() {
    var busca by rememberSaveable { mutableStateOf("") }
    var categoriaSelecionada by rememberSaveable {
        mutableStateOf("Todos")
    }

    val categorias = listOf(
        CategoriaHome("Todos", "✨"),
        CategoriaHome("Cuidados", "🧴"),
        CategoriaHome("Vitaminas", "🍊"),
        CategoriaHome("Bem-estar", "🌸")
    )

    val produtos = listOf(
        ProdutoHome(
            "Cuidados diários",
            "Sua rotina de autocuidado",
            "🧴",
            LightLilac
        ),
        ProdutoHome(
            "Vitaminas",
            "Cuide de você todos os dias",
            "🍊",
            LightPink
        ),
        ProdutoHome(
            "Higiene pessoal",
            "Bem-estar em cada detalhe",
            "🫧",
            Color(0xFFE2F3EF)
        ),
        ProdutoHome(
            "Saúde e bem-estar",
            "Mais cuidado para sua rotina",
            "🌷",
            Color(0xFFFFF0D9)
        )
    )

    val produtosFiltrados = produtos.filter { produto ->
        val correspondeBusca =
            produto.nome.contains(busca, ignoreCase = true) ||
                    produto.descricao.contains(busca, ignoreCase = true)

        val correspondeCategoria = when (categoriaSelecionada) {
            "Cuidados" -> produto.nome == "Cuidados diários" ||
                    produto.nome == "Higiene pessoal"
            "Vitaminas" -> produto.nome == "Vitaminas"
            "Bem-estar" -> produto.nome == "Saúde e bem-estar"
            else -> true
        }

        correspondeBusca && correspondeCategoria
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(HopeBackground)
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(bottom = 20.dp)
        ) {
            // Cabeçalho
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 22.dp, vertical = 20.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(46.dp)
                            .clip(RoundedCornerShape(15.dp))
                            .background(LightPink),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            Icons.Default.Favorite,
                            contentDescription = "HopeLife",
                            tint = DeepPink,
                            modifier = Modifier.size(27.dp)
                        )
                    }

                    Spacer(Modifier.width(10.dp))

                    Column {
                        Text(
                            "HopeLife",
                            style = MaterialTheme.typography.titleLarge,
                            color = DeepLilac
                        )
                        Text(
                            "Cuidado que faz bem",
                            style = MaterialTheme.typography.labelSmall,
                            color = HopeTextSecondary
                        )
                    }
                }

                Surface(
                    shape = CircleShape,
                    color = LightLilac,
                    modifier = Modifier.size(46.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            Icons.Default.Person,
                            contentDescription = "Meu perfil",
                            tint = DeepLilac,
                            modifier = Modifier.size(26.dp)
                        )
                    }
                }
            }

            // Saudação
            Column(
                modifier = Modifier.padding(
                    start = 22.dp,
                    end = 22.dp,
                    top = 8.dp
                )
            ) {
                Text(
                    "Olá, que bom ter você! ♡",
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.onBackground
                )

                Spacer(Modifier.height(6.dp))

                Text(
                    "Encontre produtos para cuidar de você.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = HopeTextSecondary
                )
            }

            Spacer(Modifier.height(22.dp))

            // Busca funcional sobre os itens demonstrativos
            OutlinedTextField(
                value = busca,
                onValueChange = { busca = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 22.dp),
                singleLine = true,
                shape = RoundedCornerShape(17.dp),
                placeholder = {
                    Text("O que você está procurando?")
                },
                leadingIcon = {
                    Icon(
                        Icons.Default.Search,
                        contentDescription = "Buscar"
                    )
                },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = DeepLilac,
                    unfocusedBorderColor = HopeOutline,
                    focusedLeadingIconColor = DeepLilac,
                    unfocusedLeadingIconColor = HopeTextSecondary,
                    cursorColor = DeepLilac
                )
            )

            Spacer(Modifier.height(26.dp))

            // Banner principal
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 22.dp),
                shape = RoundedCornerShape(25.dp),
                colors = CardDefaults.cardColors(
                    containerColor = LightLilac
                )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            "Seu bem-estar importa",
                            style = MaterialTheme.typography.titleLarge,
                            color = DeepLilac
                        )

                        Spacer(Modifier.height(8.dp))

                        Text(
                            "Pequenos cuidados fazem a diferença todos os dias.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurface
                        )

                        Spacer(Modifier.height(12.dp))

                        Surface(
                            shape = RoundedCornerShape(50.dp),
                            color = Color.White
                        ) {
                            Text(
                                "Cuidar faz bem ♡",
                                modifier = Modifier.padding(
                                    horizontal = 14.dp,
                                    vertical = 8.dp
                                ),
                                style = MaterialTheme.typography.labelLarge,
                                color = DeepLilac
                            )
                        }
                    }

                    Spacer(Modifier.width(8.dp))

                    Text(
                        "🌷",
                        style = MaterialTheme.typography.headlineLarge
                    )
                }
            }

            Spacer(Modifier.height(28.dp))

            // Categorias
            Text(
                "Explore por categoria",
                modifier = Modifier.padding(horizontal = 22.dp),
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onBackground
            )

            Spacer(Modifier.height(14.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState())
                    .padding(horizontal = 22.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                categorias.forEach { categoria ->
                    val selecionada =
                        categoriaSelecionada == categoria.nome

                    Surface(
                        modifier = Modifier
                            .clip(RoundedCornerShape(50.dp))
                            .clickable {
                                categoriaSelecionada = categoria.nome
                            },
                        shape = RoundedCornerShape(50.dp),
                        color = if (selecionada) {
                            DeepLilac
                        } else {
                            Color.White
                        },
                        border = if (selecionada) null else
                            androidx.compose.foundation.BorderStroke(
                                1.dp,
                                HopeOutline
                            )
                    ) {
                        Text(
                            "${categoria.emoji}  ${categoria.nome}",
                            modifier = Modifier.padding(
                                horizontal = 16.dp,
                                vertical = 11.dp
                            ),
                            style = MaterialTheme.typography.labelLarge,
                            color = if (selecionada) {
                                Color.White
                            } else {
                                DeepLilac
                            }
                        )
                    }
                }
            }

            Spacer(Modifier.height(28.dp))

            // Produtos demonstrativos
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 22.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    "Descubra nossos produtos",
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onBackground
                )

                Text(
                    "${produtosFiltrados.size} itens",
                    style = MaterialTheme.typography.labelMedium,
                    color = HopeTextSecondary
                )
            }

            Spacer(Modifier.height(14.dp))

            if (produtosFiltrados.isEmpty()) {
                Text(
                    "Não encontramos itens para essa busca.",
                    modifier = Modifier.padding(
                        horizontal = 22.dp,
                        vertical = 20.dp
                    ),
                    color = HopeTextSecondary
                )
            } else {
                produtosFiltrados.chunked(2).forEach { linha ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(
                                horizontal = 22.dp,
                                vertical = 6.dp
                            ),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        linha.forEach { produto ->
                            Card(
                                modifier = Modifier.weight(1f),
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
                                            .background(produto.cor),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            produto.emoji,
                                            style = MaterialTheme.typography.headlineLarge
                                        )
                                    }

                                    Spacer(Modifier.height(12.dp))

                                    Text(
                                        produto.nome,
                                        style = MaterialTheme.typography.titleMedium,
                                        color = DeepLilac
                                    )

                                    Spacer(Modifier.height(4.dp))

                                    Text(
                                        produto.descricao,
                                        style = MaterialTheme.typography.bodySmall,
                                        color = HopeTextSecondary
                                    )
                                }
                            }
                        }

                        if (linha.size == 1) {
                            Spacer(Modifier.weight(1f))
                        }
                    }
                }
            }

            Spacer(Modifier.height(16.dp))

            Text(
                "Sua saúde merece carinho. 💗",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 22.dp),
                style = MaterialTheme.typography.bodyMedium,
                color = HopeTextSecondary
            )
        }
    }
}
