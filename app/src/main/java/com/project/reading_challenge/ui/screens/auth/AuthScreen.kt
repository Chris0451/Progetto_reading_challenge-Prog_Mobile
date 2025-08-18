package com.project.reading_challenge.ui.screens.auth

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.DividerDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel

/**
 * Route: ottiene il ViewModel via Hilt e collega la screen.
 * Non ridichiara il ViewModel (definito in AuthViewModel.kt).
 */
@Composable
fun AuthRoute(
    vm: AuthViewModel = hiltViewModel()
) {
    val ui by vm.state.collectAsState()

    // Se ti serve un effetto al mount, puoi usarlo qui
    LaunchedEffect(Unit) { /* no-op */ }

    AuthScreen(
        state = ui,
        onSignUp = vm::signUp,
        onSignIn = vm::signIn,
        onReset = vm::resetPassword
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AuthScreen(
    state: AuthUiState,
    onSignUp: (email: String, password: String, name: String?, surname: String?, username: String) -> Unit,
    onSignIn: (email: String, password: String) -> Unit,
    onReset: (email: String) -> Unit
) {
    var email by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    var name by rememberSaveable { mutableStateOf("") }
    var surname by rememberSaveable { mutableStateOf("") }
    var username by rememberSaveable { mutableStateOf("") }

    Scaffold(
        topBar = { TopAppBar(title = { Text("Autenticazione") }) }
    ) { padding ->
        Column(
            Modifier
                .padding(padding)
                .padding(16.dp)
        ) {
            // --- Registrazione ---
            Text("Registrazione", style = MaterialTheme.typography.titleLarge)

            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email") },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Password") },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = username,
                onValueChange = { username = it },
                label = { Text("Username") },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Nome") },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = surname,
                onValueChange = { surname = it },
                label = { Text("Cognome") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(8.dp))

            Button(
                onClick = {
                    onSignUp(
                        email.trim(),
                        password,
                        name.ifBlank { null },
                        surname.ifBlank { null },
                        username.trim()
                    )
                },
                enabled = !state.isLoading,
                modifier = Modifier.fillMaxWidth()
            ) { Text("Crea account") }

            HorizontalDivider(
                modifier = Modifier.padding(vertical = 16.dp),
                thickness = DividerDefaults.Thickness,
                color = DividerDefaults.color
            )

            // --- Accesso ---
            Text("Accesso", style = MaterialTheme.typography.titleLarge)

            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email") },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Password") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(8.dp))

            Button(
                onClick = { onSignIn(email.trim(), password) },
                enabled = !state.isLoading,
                modifier = Modifier.fillMaxWidth()
            ) { Text("Accedi") }

            TextButton(onClick = { onReset(email.trim()) }) {
                Text("Password dimenticata?")
            }

            // --- Stato UI ---
            if (state.isLoading) {
                Spacer(Modifier.height(12.dp))
                LinearProgressIndicator(Modifier.fillMaxWidth())
            }

            state.error?.let {
                Spacer(Modifier.height(8.dp))
                Text("Errore: $it", color = MaterialTheme.colorScheme.error)
            }
        }
    }
}

@Preview(showBackground = true, name = "Auth – Light")
@Composable
private fun AuthScreenPreview() {
    val previewState = AuthUiState(isLoading = false, error = null)
    MaterialTheme {
        AuthScreen(
            state = previewState,
            onSignUp = { _, _, _, _, _ -> },
            onSignIn = { _, _ -> },
            onReset = { _ -> }
        )
    }
}
