package com.project.reading_challenge.domain.model

data class FriendLink(
    val uid: String = "",                         // amico (friendUid)
    val status: FriendStatus = FriendStatus.PENDING,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)
