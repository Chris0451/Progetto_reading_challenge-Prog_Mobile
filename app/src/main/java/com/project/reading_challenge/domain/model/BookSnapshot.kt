package com.project.reading_challenge.domain.model

data class BookSnapshot(
    val title: String = "",
    val authors: List<String> = emptyList(),
    val thumbnail: String? = null,
    val categories: List<String> = emptyList(),
    val pageCount: Int? = null,
    val publishedDate: String? = null
)
