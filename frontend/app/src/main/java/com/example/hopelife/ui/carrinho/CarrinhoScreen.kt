
package com.example.hopelife.ui.carrinho

import androidx.browser.customtabs.CustomTabsIntent
import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import java.text.NumberFormat
import java.util.Locale

@Composable
fun CarrinhoScreen(carrinhoViewModel: CarrinhoViewModel) {
    val itens by carrinhoViewModel.itens.collectAsState()
    val checkoutState by carrinhoViewModel.checkoutState.collectAsState()

    val context = LocalContext.current

    val formatoMoeda = NumberFormat.getCurrencyInstance(Locale("pt", "BR"))

    val subtotal = itens.sumOf { item ->
        item.produto.preco.multiply(java.math.BigDecimal(item.quantidade))
    }

    LaunchedEffect(checkoutState.checkoutUrl) {
        val url = checkoutState.checkoutUrl

        if (!url.isNullOrBlank()) {
            try {
                val customTabsIntent = CustomTabsIntent.Builder().build()
                customTabsIntent.launchUrl(context, Uri.parse(url))
                carrinhoViewModel.limparResultadoCheckout()
            } catch (e: Exception) {
                carrinhoViewModel.limparResultadoCheckout()
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)
    ) {
        Text(
            "Meu carrinho",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(20.dp))

        if (itens.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text("Seu carrinho está vazio 🛒")
            }
        } else {
            itens.forEach { item ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 12.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            item.produto.nome,
                            style = MaterialTheme.typography.titleMedium
                        )

                        Text(formatoMoeda.format(item.produto.preco))

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            TextButton(
                                onClick = {
                                    carrinhoViewModel.diminuirQuantidade(item.produto.id)
                                }
                            ) {
                                Text("−")
                            }

                            Text(item.quantidade.toString())

                            TextButton(
                                onClick = {
                                    carrinhoViewModel.aumentarQuantidade(item.produto.id)
                                }
                            ) {
                                Text("+")
                            }

                            Spacer(modifier = Modifier.weight(1f))

                            TextButton(
                                onClick = {
                                    carrinhoViewModel.removerProduto(item.produto.id)
                                }
                            ) {
                                Text("Remover")
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "Subtotal: ${formatoMoeda.format(subtotal)}",
                style = MaterialTheme.typography.titleLarge
            )

            Spacer(modifier = Modifier.height(20.dp))

            checkoutState.mensagem?.let { mensagem ->
                Text(
                    text = mensagem,
                    color = if (checkoutState.pedidoId != null) {
                        MaterialTheme.colorScheme.primary
                    } else {
                        MaterialTheme.colorScheme.error
                    }
                )

                Spacer(modifier = Modifier.height(12.dp))
            }

            Button(
                onClick = {
                    carrinhoViewModel.finalizarCompra()
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = !checkoutState.carregando
            ) {
                if (checkoutState.carregando) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp),
                        strokeWidth = 2.dp
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Criando pedido...")
                } else {
                    Text("Finalizar compra")
                }
            }
        }
    }
}
