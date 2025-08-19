package com.project.reading_challenge

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.hilt.navigation.compose.hiltViewModel
import dagger.hilt.android.AndroidEntryPoint
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.project.reading_challenge.ui.screens.auth.old.AuthRoute

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MaterialTheme {
                Surface(color = MaterialTheme.colorScheme.background) {
                    val nav = rememberNavController()

                    NavHost(
                        navController = nav,
                        startDestination = "auth"
                    ) {
                        // Auth screen
                        composable("auth") {
                            // usa il VM di Hilt all’interno della composable
                            AuthRoute(
                                vm = hiltViewModel(),
                            )
                        }

                        // Esempio di rotta futura:
                        // composable("home") { HomeRoute(vm = hiltViewModel(), onOpenBook = { ... }) }
                    }
                }
            }
        }
    }
}
