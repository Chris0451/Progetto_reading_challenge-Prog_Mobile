package com.project.reading_challenge.domain.model

data class UserProfile(
    val uid: String = "",
    val name: String? = null,
    val surname: String? = null,
    val email: String? = null,
    val username: String? = null,
    val avatarUrl: String? = null,
    val createdAt: Long = System.currentTimeMillis()
)
