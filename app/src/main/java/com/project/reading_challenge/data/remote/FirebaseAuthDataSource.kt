package com.project.reading_challenge.data.remote

import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class FirebaseAuthDataSource(
    private val auth: FirebaseAuth,
) {
    fun authState(): Flow<String?> = callbackFlow {
        val listener = FirebaseAuth.AuthStateListener { trySend(it.currentUser?.uid) }
        auth.addAuthStateListener(listener)
        awaitClose { auth.removeAuthStateListener(listener) }
    }

    suspend fun signUpEmail(email: String, password: String) =
        auth.createUserWithEmailAndPassword(email, password).await().user
            ?: error("User creation failed")

    suspend fun signInEmail(email: String, password: String) =
        auth.signInWithEmailAndPassword(email, password).await()

    suspend fun signOut() = auth.signOut()

    suspend fun sendEmailVerification() = auth.currentUser?.sendEmailVerification()?.await()

    suspend fun sendPasswordReset(email: String) =
        auth.sendPasswordResetEmail(email).await()

    fun currentUid(): String? = auth.currentUser?.uid
}