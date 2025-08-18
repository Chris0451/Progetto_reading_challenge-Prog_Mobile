package com.project.reading_challenge.ui.screens.catalog

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.project.reading_challenge.data.remote.VolumeItem
import com.project.reading_challenge.data.repo.CatalogRepository
import com.project.reading_challenge.data.repo.UserPreferencesRepository
import com.project.reading_challenge.domain.model.BookSnapshot
import com.project.reading_challenge.domain.model.UserPreferences
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

@HiltViewModel
class CatalogViewModel @Inject constructor(
    private val prefsRepo: UserPreferencesRepository,
    private val catalogRepo: CatalogRepository
) : ViewModel() {

    private val _state = MutableStateFlow(CatalogUiState())
    val state = _state.asStateFlow()

    fun load() = viewModelScope.launch {
        _state.update { it.copy(isLoading = true, error = null) }
        try {
            val prefs = prefsRepo.getPreferences() ?: UserPreferences()
            val items: List<VolumeItem> = catalogRepo.fetchRecommendations(prefs)
            _state.update { it.copy(isLoading = false, items = items) }
        } catch (e: Exception) {
            _state.update { it.copy(isLoading = false, error = e.message ?: "Errore sconosciuto") }
        }
    }
}