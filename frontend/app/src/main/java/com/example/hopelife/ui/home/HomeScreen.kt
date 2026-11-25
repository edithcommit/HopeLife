
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
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.example.hopelife.R
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
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import androidx.compose.ui.layout.ContentScale
import androidx.compose.runtime.remember
import com.example.hopelife.ui.carrinho.CarrinhoViewModel
import androidx.compose.material3.Button
import androidx.compose.foundation.layout.fillMaxWidth
import android.widget.Toast
import androidx.compose.material3.IconButton
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.foundation.Image
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.graphics.Brush
import com.example.hopelife.ui.theme.DeepLilac
import com.example.hopelife.ui.theme.DeepPink
import com.example.hopelife.ui.theme.HopeBackground
import com.example.hopelife.ui.theme.HopeOutline
import com.example.hopelife.ui.theme.HopeTextSecondary
import com.example.hopelife.ui.theme.LightLilac
import com.example.hopelife.ui.theme.LightPink

@Composable
fun HomeScreen(
    viewModel: ProdutoViewModel,
    carrinhoViewModel: CarrinhoViewModel
) {

    var busca by rememberSaveable {
        mutableStateOf("")
    }

    var categoriaSelecionada by rememberSaveable {
        mutableStateOf("Todos")
    }

    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current
    val idsFavoritos = uiState.favoritos.map { it.id }.toSet()

    LaunchedEffect(Unit) {
        viewModel.carregarProdutos()
    }

    LaunchedEffect(uiState.mensagemFavorito) {
        uiState.mensagemFavorito?.let { mensagem ->
            Toast.makeText(
                context,
                mensagem,
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    val categorias = listOf("Todos") +
            uiState.produtos
                .mapNotNull { it.categoria?.trim() }
                .filter { it.isNotBlank() }
                .distinctBy { it.lowercase() }
                .sorted()

    // Filtragem dos produtos por nome, descrição e categoria
    val produtosFiltrados = uiState.produtos.filter { produto ->

        val correspondeBusca =
            produto.nome.contains(busca, ignoreCase = true) ||
                    (produto.descricao ?: "")
                        .contains(busca, ignoreCase = true)

        val correspondeCategoria =
            categoriaSelecionada == "Todos" ||
                    produto.categoria == categoriaSelecionada

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

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {

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
                    Box(
                        contentAlignment = Alignment.Center
                    ) {
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
                    "Olá, que bom ter você!",
                    style = MaterialTheme.typography.headlineMedium,
                    color = DeepLilac
                )

                Spacer(Modifier.height(6.dp))

                Text(
                    "Encontre produtos para cuidar de você.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = HopeTextSecondary
                )
            }

            Spacer(Modifier.height(22.dp))

            // Barra de pesquisa
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
                    cursorColor = DeepLilac,
                    focusedTextColor = Color(0xFF342747),
                    unfocusedTextColor = Color(0xFF342747),
                    focusedPlaceholderColor = HopeTextSecondary,
                    unfocusedPlaceholderColor = HopeTextSecondary
                )
            )

            Spacer(Modifier.height(26.dp))


// Banner principal
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 22.dp)
                    .height(170.dp),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(
                    containerColor = LightLilac
                )
            ) {

                Box(
                    modifier = Modifier.fillMaxSize()
                ) {

                    // SUA IMAGEM AQUI:
                    // Coloque o arquivo banner_hopelife.jpg
                    // na pasta res/drawable

                    Image(
                        painter = painterResource(
                            id = R.drawable.banner_hopelife
                        ),
                        contentDescription = "Imagem de bem-estar",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .fillMaxSize()
                    )

                    // Camada de transparência para melhorar a leitura
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                Brush.horizontalGradient(
                                    colors = listOf(
                                        Color(0xFFF4ECFC).copy(alpha = 0.96f),
                                        Color(0xFFF4ECFC).copy(alpha = 0.78f),
                                        Color.Transparent
                                    ),
                                    startX = 0f,
                                    endX = 650f
                                )
                            )
                    )

                    // Textos do banner
                    Column(
                        modifier = Modifier
                            .fillMaxWidth(0.75f)
                            .align(Alignment.CenterStart)
                            .padding(start = 17.dp, end = 4.dp)
                    ) {

                        Text(
                            text = "Seu bem-estar importa",
                            style = MaterialTheme.typography.titleLarge,
                            color = Color(0xFF49316B)
                        )

                        Spacer(Modifier.height(7.dp))

                        Text(
                            text = "Pequenos cuidados fazem a diferença todos os dias.",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color(0xFF49316B)
                        )

                        Spacer(Modifier.height(12.dp))

                        Surface(
                            shape = RoundedCornerShape(50.dp),
                            color = Color.White
                        ) {

                            Text(
                                text = "Cuidar faz bem",
                                modifier = Modifier.padding(
                                    horizontal = 14.dp,
                                    vertical = 8.dp
                                ),
                                style = MaterialTheme.typography.labelLarge,
                                color = Color(0xFF6841A5)
                            )
                        }
                    }
                }
            }


            Spacer(Modifier.height(28.dp))

            // Categorias
            Text(
                "Explore por categoria",
                modifier = Modifier.padding(horizontal = 22.dp),
                style = MaterialTheme.typography.titleLarge,
                color = DeepLilac
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
                        categoriaSelecionada == categoria

                    Surface(
                        modifier = Modifier
                            .clip(RoundedCornerShape(50.dp))
                            .clickable {
                                categoriaSelecionada = categoria
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
                            "${if (categoria == "Todos") "" else "•"} $categoria",
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

            // Título e quantidade de produtos
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
                    color = DeepLilac
                )

                Text(
                    "${produtosFiltrados.size} itens",
                    style = MaterialTheme.typography.labelMedium,
                    color = HopeTextSecondary
                )
            }

            Spacer(Modifier.height(14.dp))

            // Estados de carregamento, erro e lista de produtos
            when {

                uiState.carregando && uiState.produtos.isEmpty() -> {

                    Text(
                        "Carregando produtos...",
                        modifier = Modifier.padding(
                            horizontal = 22.dp,
                            vertical = 20.dp
                        ),
                        color = HopeTextSecondary
                    )
                }

                uiState.mensagemErro != null &&
                        uiState.produtos.isEmpty() -> {

                    Text(
                        uiState.mensagemErro ?: "Erro ao carregar produtos.",
                        modifier = Modifier.padding(
                            horizontal = 22.dp,
                            vertical = 20.dp
                        ),
                        color = HopeTextSecondary
                    )
                }

                produtosFiltrados.isEmpty() -> {

                    Text(
                        "Não encontramos itens para essa busca.",
                        modifier = Modifier.padding(
                            horizontal = 22.dp,
                            vertical = 20.dp
                        ),
                        color = HopeTextSecondary
                    )
                }

                else -> {

                    produtosFiltrados.chunked(2).forEach { linha ->

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(
                                    horizontal = 22.dp,
                                    vertical = 6.dp
                                ),
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                            verticalAlignment = Alignment.Top
                        ) {

                            linha.forEach { produto ->

                                Card(
                                    modifier = Modifier
                                        .weight(1f)
                                        .height(330.dp),
                                    shape = RoundedCornerShape(20.dp),
                                    colors = CardDefaults.cardColors(
                                        containerColor = Color.White
                                    ),
                                    elevation = CardDefaults.cardElevation(
                                        defaultElevation = 1.dp
                                    )
                                ) {

                                    Column(
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .padding(12.dp)
                                    ) {

                                        var imagemFalhou by remember(produto.imagemUrl) {
                                            mutableStateOf(false)
                                        }

                                        // Imagem do produto
                                        Box(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .height(100.dp)
                                                .clip(RoundedCornerShape(13.dp))
                                                .background(Color(0xFFFAF8FC)),
                                            contentAlignment = Alignment.Center
                                        ) {

                                            if (produto.imagemUrl.isNullOrBlank() || imagemFalhou) {

                                                Text(
                                                    "Imagem indisponível",
                                                    style = MaterialTheme.typography.labelSmall,
                                                    color = HopeTextSecondary
                                                )

                                            } else {

                                                AsyncImage(
                                                    model = produto.imagemUrl,
                                                    contentDescription = produto.nome,
                                                    contentScale = ContentScale.Fit,
                                                    modifier = Modifier
                                                        .fillMaxSize()
                                                        .padding(5.dp),
                                                    onError = {
                                                        imagemFalhou = true
                                                    }
                                                )
                                            }

                                            // Botão de favoritos
                                            Surface(
                                                modifier = Modifier
                                                    .align(Alignment.TopEnd)
                                                    .padding(5.dp)
                                                    .size(34.dp),
                                                shape = CircleShape,
                                                color = Color.White,
                                                shadowElevation = 2.dp
                                            ) {

                                                IconButton(
                                                    onClick = {
                                                        viewModel.alternarFavorito(produto)
                                                    },
                                                    enabled = produto.id !in uiState.operacoesFavorito
                                                ) {

                                                    Icon(
                                                        imageVector = if (produto.id in idsFavoritos) {
                                                            Icons.Default.Favorite
                                                        } else {
                                                            Icons.Outlined.FavoriteBorder
                                                        },
                                                        contentDescription = if (produto.id in idsFavoritos) {
                                                            "Remover dos favoritos"
                                                        } else {
                                                            "Adicionar aos favoritos"
                                                        },
                                                        tint = if (produto.id in idsFavoritos) {
                                                            DeepPink
                                                        } else {
                                                            HopeTextSecondary
                                                        },
                                                        modifier = Modifier.size(20.dp)
                                                    )
                                                }
                                            }
                                        }

                                        Spacer(Modifier.height(10.dp))

                                        // Nome com altura padronizada
                                        Text(
                                            text = produto.nome,
                                            style = MaterialTheme.typography.titleSmall,
                                            color = DeepLilac,
                                            maxLines = 2,
                                            minLines = 2,
                                            overflow = TextOverflow.Ellipsis,
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .height(40.dp)
                                        )

                                        Spacer(Modifier.height(6.dp))

                                        // Botão de adicionar ao carrinho
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
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .height(42.dp),
                                            enabled = produto.quantidadeEstoque > 0
                                        ) {

                                            Text(
                                                text = if (produto.quantidadeEstoque > 0) {
                                                    "Adicionar ao carrinho"
                                                } else {
                                                    "Indisponível"
                                                },
                                                style = MaterialTheme.typography.labelMedium,
                                                maxLines = 2
                                            )
                                        }

                                        Spacer(Modifier.height(7.dp))

                                        // Espaço reservado para a informação de receita
                                        Box(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .height(27.dp),
                                            contentAlignment = Alignment.CenterStart
                                        ) {

                                            if (produto.exigeReceita) {

                                                Surface(
                                                    shape = RoundedCornerShape(7.dp),
                                                    color = Color(0xFFFFEEEE)
                                                ) {

                                                    Text(
                                                        text = "Receita obrigatória",
                                                        modifier = Modifier.padding(
                                                            horizontal = 7.dp,
                                                            vertical = 5.dp
                                                        ),
                                                        style = MaterialTheme.typography.labelSmall,
                                                        color = Color(0xFFB3261E),
                                                        maxLines = 1
                                                    )
                                                }
                                            }
                                        }

                                        Spacer(Modifier.height(3.dp))

                                        // Descrição com altura fixa
                                        Text(
                                            text = produto.descricao
                                                ?: "Sem descrição disponível",
                                            style = MaterialTheme.typography.bodySmall,
                                            color = HopeTextSecondary,
                                            maxLines = 2,
                                            minLines = 2,
                                            overflow = TextOverflow.Ellipsis,
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .height(34.dp)
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
            }

            Spacer(Modifier.height(16.dp))

            Text(
                "Sua saúde merece cuidado todos os dias.",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 22.dp),
                style = MaterialTheme.typography.bodyMedium,
                color = HopeTextSecondary
            )
        }
    }
}
