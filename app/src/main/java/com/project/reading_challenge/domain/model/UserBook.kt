package com.project.reading_challenge.domain.model

data class UserBook(
    val id: String = "",               // = documentId in users/{uid}/books
    val volumeId: String = "",         // ID Google Books (es. "zyTCAlFPjgYC")
    val snapshot: BookSnapshot? = null, // opzionale (per liste/offline)
    val shelfIds: List<String> = emptyList(), // appartenenza a più scaffali
    val status: ReadingStatus = ReadingStatus.TO_READ,
    val progress: Int = 0,             // % o pagine lette (decidi in UI)
    val personalRating: Int? = null,   // 1..5 opzionale (voto personale)
    val notesCount: Int = 0,
    val updatedAt: Long = System.currentTimeMillis()
)
