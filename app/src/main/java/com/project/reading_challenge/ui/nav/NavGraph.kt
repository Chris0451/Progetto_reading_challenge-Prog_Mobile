package com.project.reading_challenge.ui.nav

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.project.reading_challenge.ui.screens.auth.AuthRoute
import com.project.reading_challenge.ui.screens.catalog.CatalogRoute

sealed class Screen(val route: String) {
    object Auth : Screen("auth")
    object Home : Screen("home")
    object Catalog : Screen("catalog")       // <-- nuova voce
    object Detail : Screen("detail/{id}") {
        fun create(id: String) = "detail/$id"
    }
}

@Composable
fun AppNavHost(
    navController: NavHostController,
    start: String = Screen.Auth.route
) {
    NavHost(navController, startDestination = start) {
        composable(Screen.Auth.route) { AuthRoute() }
        composable(Screen.Home.route) { /* HomeRoute(...) */ }
        composable(Screen.Catalog.route) {
            CatalogRoute(onOpenBook = { id -> navController.navigate(Screen.Detail.create(id)) })
        }
        composable(Screen.Detail.route) { backStack ->
            val id = backStack.arguments?.getString("id").orEmpty()
            /* DetailRoute(id = id) */
        }
    }
}
