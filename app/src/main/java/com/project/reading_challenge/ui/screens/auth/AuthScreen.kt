package com.project.reading_challenge.ui.screens.auth

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dagger.hilt.android.lifecycle.HiltViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

// --- UI STATE (semplificato per preview) ---
data class AuthUiState(
    val error: String? = null,
    val isLoading: Boolean = false
)

// --- VIEWMODEL (esempio minimal: adatta al tuo AuthRepository reale) ---
@HiltViewModel
class AuthViewModel @Inject constructor(
    // private val repo: AuthRepository
) : ViewModel() {
    private val _state = MutableStateFlow(AuthUiState())
    val state: StateFlow<AuthUiState> = _state

    fun signUp(email: String, password: String, name: String?, surname: String?, username: String) {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true, error = null)
            // repo.signUpWithEmail(...)
            _state.value = _state.value.copy(isLoading = false)
        }
    }

    fun signIn(email: String, password: String) {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true, error = null)
            // repo.signInWithEmailAndPassword(...)
            _state.value = _state.value.copy(isLoading = false)
        }
    }

    fun resetPassword(email: String) {
        viewModelScope.launch {
            // repo.sendPasswordReset(email)
        }
    }
}

// --- ROUTE: usa Hilt VM e collega la Screen ---
@Composable
fun AuthRoute(
    vm: AuthViewModel = androidx.hilt.navigation.compose.hiltViewModel()
) {
    val ui by vm.state.collectAsState()
    AuthScreen(
        state = ui,
        onSignUp = vm::signUp,
        onSignIn = vm::signIn,
        onReset = vm::resetPassword
    )
}

// --- SCREEN: UI pura (facile da fare preview) ---
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
        Column(Modifier.padding(padding).padding(16.dp)) {
            Text("Registrazione", style = MaterialTheme.typography.titleLarge)
            OutlinedTextField(email, { email = it }, label = { Text("Email") }, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(password, { password = it }, label = { Text("Password") }, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(username, { username = it }, label = { Text("Username") }, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(name, { name = it }, label = { Text("Nome") }, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(surname, { surname = it }, label = { Text("Cognome") }, modifier = Modifier.fillMaxWidth())
            Spacer(Modifier.height(8.dp))
            Button(
                onClick = { onSignUp(email.trim(), password, name.ifBlank { null }, surname.ifBlank { null }, username.trim()) },
                enabled = !state.isLoading,
                modifier = Modifier.fillMaxWidth()
            ) { Text("Crea account") }

            Divider(Modifier.padding(vertical = 16.dp))

            Text("Accesso", style = MaterialTheme.typography.titleLarge)
            OutlinedTextField(email, { email = it }, label = { Text("Email") }, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(password, { password = it }, label = { Text("Password") }, modifier = Modifier.fillMaxWidth())
            Spacer(Modifier.height(8.dp))
            Button(
                onClick = { onSignIn(email.trim(), password) },
                enabled = !state.isLoading,
                modifier = Modifier.fillMaxWidth()
            ) { Text("Accedi") }

            TextButton(onClick = { onReset(email.trim()) }) { Text("Password dimenticata?") }

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

// --- PREVIEW: non richiede Hilt né ViewModel reale ---
@Preview(showBackground = true, name = "Auth – Light")
@Composable
private fun AuthScreenPreview() {
    val fake = AuthUiState(isLoading = false, error = null)
    MaterialTheme {
        AuthScreen(
            state = fake,
            onSignUp = { _,_,_,_,_ -> },
            onSignIn = { _, _ -> },
            onReset = { _ -> }
        )
    }
}
