package com.project.reading_challenge.ui.screens.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.project.reading_challenge.data.repo.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
class AuthViewModel @Inject constructor(
    private val repo: AuthRepository
) : ViewModel() {
    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    fun signUp(email: String, password: String, name: String?, surname: String?, username: String) {
        viewModelScope.launch {
            runCatching {
                repo.signUpWithEmail(email, password, name, surname, username)
            }.onFailure { _error.value = it.message }
        }
    }

    fun signIn(email: String, password: String) {
        viewModelScope.launch {
            runCatching { repo.signInWithEmail(email, password) }
                .onFailure { _error.value = it.message }
        }
    }

    fun resetPassword(email: String) {
        viewModelScope.launch {
            runCatching { repo.sendPasswordReset(email) }
                .onFailure { _error.value = it.message }
        }
    }
}