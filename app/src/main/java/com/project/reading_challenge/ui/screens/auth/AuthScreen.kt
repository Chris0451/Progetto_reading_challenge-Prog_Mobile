package com.project.reading_challenge.ui.screens.auth

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun AuthScreen(
    vm: AuthViewModel
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var name by remember { mutableStateOf("") }
    var surname by remember { mutableStateOf("") }
    var username by remember { mutableStateOf("") }

    val error by vm.error.collectAsState()

    Column(Modifier.padding(16.dp)) {
        Text("Registrazione", style = MaterialTheme.typography.titleLarge)
        OutlinedTextField(email, { email = it }, label = { Text("Email") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(password, { password = it }, label = { Text("Password") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(username, { username = it }, label = { Text("Username") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(name, { name = it }, label = { Text("Nome") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(surname, { surname = it }, label = { Text("Cognome") }, modifier = Modifier.fillMaxWidth())
        Spacer(Modifier.height(8.dp))
        Button(
            onClick = { vm.signUp(email.trim(), password, name.ifBlank { null }, surname.ifBlank { null }, username.trim()) },
            modifier = Modifier.fillMaxWidth()
        ) { Text("Crea account") }

        HorizontalDivider(
            Modifier.padding(vertical = 16.dp),
            DividerDefaults.Thickness,
            DividerDefaults.color
        )

        Text("Accesso", style = MaterialTheme.typography.titleLarge)
        OutlinedTextField(email, { email = it }, label = { Text("Email") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(password, { password = it }, label = { Text("Password") }, modifier = Modifier.fillMaxWidth())
        Spacer(Modifier.height(8.dp))
        Button(onClick = { vm.signIn(email.trim(), password) }, modifier = Modifier.fillMaxWidth()) { Text("Accedi") }

        TextButton(onClick = { vm.resetPassword(email.trim()) }) { Text("Password dimenticata?") }

        if (error != null) {
            Text("Errore: $error", color = MaterialTheme.colorScheme.error)
        }
    }
}

