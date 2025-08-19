package com.project.reading_challenge.ui.screens.catalog.old

import com.project.reading_challenge.data.remote.VolumeItem

data class CatalogUiState(
    val isLoading: Boolean = false,
    val items: List<VolumeItem> = emptyList(),
    val error: String? = null
)
