package com.project.reading_challenge.domain.model

data class Review(
    val id: String = "",               // = documentId in books/{volumeId}/reviews
    val volumeId: String = "",         // ripetuto per query/denormalizzazione
    val authorUid: String = "",        // chi ha scritto
    val rating: Int = 0,               // 1..5
    val text: String = "",
    val likesCount: Int = 0,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)
