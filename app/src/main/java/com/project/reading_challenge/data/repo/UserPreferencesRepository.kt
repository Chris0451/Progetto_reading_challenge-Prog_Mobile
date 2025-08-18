package com.project.reading_challenge.data.repo

import com.project.reading_challenge.data.remote.FirebaseAuthDataSource
import com.project.reading_challenge.data.remote.FirestoreDataSource
import com.project.reading_challenge.domain.model.UserPreferences
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserPreferencesRepository @Inject constructor(
    private val authDs: FirebaseAuthDataSource,
    private val fds: FirestoreDataSource
) {
    private fun requireUid() = authDs.currentUid() ?: error("Not signed in")

    private fun prefDoc() = fds.userDoc(requireUid())
        .collection("preferences").document("profile")

    suspend fun setPreferences(prefs: UserPreferences) {
        prefDoc().set(prefs).await()
    }

    suspend fun getPreferences(): UserPreferences? {
        val uid = authDs.currentUid() ?: return null
        return fds.getUserPreferences(uid)
    }

    fun observePreferences(): Flow<UserPreferences?> = callbackFlow {
        val reg = prefDoc().addSnapshotListener { snap, _ ->
            trySend(snap?.toObject(UserPreferences::class.java))
        }
        awaitClose { reg.remove() }
    }
}