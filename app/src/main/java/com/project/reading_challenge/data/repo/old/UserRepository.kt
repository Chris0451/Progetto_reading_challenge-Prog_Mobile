package com.project.reading_challenge.data.repo.old

import com.project.reading_challenge.data.remote.FirebaseAuthDataSource
import com.project.reading_challenge.data.remote.FirestoreDataSource
import com.project.reading_challenge.data.remote.observeDoc
import com.project.reading_challenge.domain.model.UserProfile
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.tasks.await

class UserRepository(
    private val authDs: FirebaseAuthDataSource,
    private val fds: FirestoreDataSource
) {
    private fun requireUid() = authDs.currentUid() ?: error("Not signed in")

    suspend fun upsertProfile(profile: UserProfile) {
        val uid = requireUid()
        fds.set(fds.userDoc(uid), profile)
    }

    suspend fun getProfile(): UserProfile? {
        val uid = requireUid()
        return fds.userDoc(uid).get().await().toObject(UserProfile::class.java)
    }

    fun observeProfile(): Flow<UserProfile?> =
        observeDoc<UserProfile>(fds.userDoc(requireUid()))
}