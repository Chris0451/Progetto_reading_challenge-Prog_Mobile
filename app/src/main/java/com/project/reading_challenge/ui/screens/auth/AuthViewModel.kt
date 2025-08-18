package com.project.reading_challenge.ui.screens.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.project.reading_challenge.data.repo.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class AuthUiState(
    val isLoading: Boolean = false,
    val error: String? = null
)

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val repo: AuthRepository
) : ViewModel() {

    private val _state = MutableStateFlow(AuthUiState())
    val state: StateFlow<AuthUiState> = _state.asStateFlow()

    fun signUp(email: String, password: String, name: String?, surname: String?, username: String) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }
            runCatching {
                repo.signUpWithEmail(email, password, name, surname, username)
            }.onSuccess {
                _state.update { it.copy(isLoading = false) }
            }.onFailure { e ->
                _state.update { it.copy(isLoading = false, error = e.message ?: "Errore sconosciuto") }
            }
        }
    }

    fun signIn(email: String, password: String) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }
            runCatching {
                repo.signInWithEmail(email, password)
            }.onSuccess {
                _state.update { it.copy(isLoading = false) }
            }.onFailure { e ->
                _state.update { it.copy(isLoading = false, error = e.message ?: "Errore sconosciuto") }
            }
        }
    }

    fun resetPassword(email: String) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }
            runCatching {
                repo.sendPasswordReset(email)
            }.onSuccess {
                _state.update { it.copy(isLoading = false) }
            }.onFailure { e ->
                _state.update { it.copy(isLoading = false, error = e.message ?: "Errore sconosciuto") }
            }
        }
    }
}
