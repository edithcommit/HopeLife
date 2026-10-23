
package com.example.hopelife.navigation

sealed class Screen(val route: String) {

    data object Login : Screen("login")

    data object Cadastro : Screen("cadastro")

    data object Home : Screen("home")

    data object Favoritos : Screen("favoritos")

    data object Carrinho : Screen("carrinho")

    data object Perfil : Screen("perfil")

    data object AreaFarmaceutico : Screen("area_farmaceutico")
}
