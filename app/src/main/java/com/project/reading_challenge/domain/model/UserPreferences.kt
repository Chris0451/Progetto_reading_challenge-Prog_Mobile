package com.project.reading_challenge.domain.model

data class UserPreferences(
    val categories: List<String> = emptyList(),
    val language: String = "it"
)
