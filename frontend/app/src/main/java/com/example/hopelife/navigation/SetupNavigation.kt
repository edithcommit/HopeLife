
package com.example.hopelife.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.compose.runtime.getValue
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.unit.dp
import androidx.compose.material3.MaterialTheme

import com.example.hopelife.data.api.RetrofitClient
import com.example.hopelife.data.repository.AuthRepository
import com.example.hopelife.ui.cadastro.CadastroScreen
import com.example.hopelife.ui.cadastro.CadastroViewModelFactory
import com.example.hopelife.ui.login.LoginScreen
import com.example.hopelife.ui.home.HomeScreen
import com.example.hopelife.ui.favoritos.FavoritosScreen
import com.example.hopelife.ui.carrinho.CarrinhoScreen
import com.example.hopelife.ui.perfil.PerfilScreen

private data class BottomItem(
    val screen: Screen,
    val label: String,
    val icon: androidx.compose.ui.graphics.vector.ImageVector
)

@Composable
fun SetupNavigation() {

    val navController = rememberNavController()

    val cadastroFactory = remember {
        CadastroViewModelFactory(
            AuthRepository(RetrofitClient.authApi)
        )
    }

    val bottomItems = listOf(
        BottomItem(Screen.Home, "Home", Icons.Default.Home),
        BottomItem(Screen.Favoritos, "Favoritos", Icons.Default.Favorite),
        BottomItem(Screen.Carrinho, "Carrinho", Icons.Default.ShoppingCart),
        BottomItem(Screen.Perfil, "Perfil", Icons.Default.Person)
    )

    val backStackEntry by
    navController.currentBackStackEntryAsState()

    val destination = backStackEntry?.destination
    val currentRoute = destination?.route

    val mostrarBarra = bottomItems.any {
        it.screen.route == currentRoute
    }

    Scaffold(
        bottomBar = {
            if (mostrarBarra) {
                NavigationBar {
                    bottomItems.forEach { item ->

                        val selecionado =
                            destination?.hierarchy?.any {
                                it.route == item.screen.route
                            } == true

                        NavigationBarItem(
                            selected = selecionado,
                            onClick = {
                                navController.navigate(item.screen.route) {
                                    popUpTo(Screen.Home.route) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            },
                            icon = {
                                Icon(
                                    imageVector = item.icon,
                                    contentDescription = item.label
                                )
                            },
                            label = { Text(item.label) }
                        )
                    }
                }
            }
        }
    ) { innerPadding ->

        NavHost(
            navController = navController,
            startDestination = Screen.Login.route,
            modifier = Modifier.padding(innerPadding)

        ) {
            composable(Screen.AreaFarmaceutico.route) {
                androidx.compose.material3.Text(
                    text = "Área do Farmacêutico",
                    modifier = Modifier.padding(24.dp),
                    style = androidx.compose.material3.MaterialTheme.typography.headlineMedium
                )
            }

            composable(Screen.Login.route) {
                LoginScreen(
                    onIrParaCadastro = {
                        navController.navigate(Screen.Cadastro.route)
                    },
                    onEntrarDemo = {
                        navController.navigate(Screen.Home.route) {
                            popUpTo(Screen.Login.route) {
                                inclusive = true
                            }
                            launchSingleTop = true
                        }
                    },

                    onLoginSucesso = { perfil ->
                        val destino = when (perfil) {
                            "FARMACEUTICO" -> Screen.AreaFarmaceutico.route
                            else -> Screen.Home.route
                        }

                        navController.navigate(destino) {
                            popUpTo(Screen.Login.route) {
                                inclusive = true
                            }
                            launchSingleTop = true
                        }
                    }

                )
            }

            composable(Screen.Cadastro.route) {
                CadastroScreen(
                    factory = cadastroFactory,
                    onIrParaLogin = {
                        navController.navigate(Screen.Login.route) {
                            popUpTo(Screen.Cadastro.route) {
                                inclusive = true
                            }
                            launchSingleTop = true
                        }
                    }
                )
            }

            composable(Screen.Home.route) {
                HomeScreen()
            }

            composable(Screen.Favoritos.route) {
                FavoritosScreen()
            }

            composable(Screen.Carrinho.route) {
                CarrinhoScreen()
            }

            composable(Screen.Perfil.route) {
                PerfilScreen(
                    onLogout = {
                        navController.navigate(Screen.Login.route) {
                            popUpTo(Screen.Home.route) {
                                inclusive = true
                            }
                            launchSingleTop = true
                        }
                    }
                )
            }
        }
    }
}
