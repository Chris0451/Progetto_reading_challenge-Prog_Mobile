package com.project.reading_challenge.domain.model

data class Shelf(
    val id: String = "",
    val name: String = "",
    val isDefault: Boolean = false,
    val createdAt: Long = System.currentTimeMillis()
)
