package com.project.reading_challenge.data.repo

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.project.reading_challenge.data.remote.FirebaseAuthDataSource
import com.project.reading_challenge.data.remote.FirestoreDataSource
import com.project.reading_challenge.domain.model.UserProfile
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.tasks.await
import java.util.Locale

class AuthRepository(
    private val authDs: FirebaseAuthDataSource,
    private val fs: FirebaseFirestore,
    private val fds: FirestoreDataSource
) {
    fun authState(): Flow<String?> = authDs.authState()
    fun currentUid(): String? = authDs.currentUid()

    suspend fun signUpWithEmail(
        email: String,
        password: String,
        name: String?,
        surname: String?,
        username: String
    ) {
        val user = authDs.signUpEmail(email, password)
        val uid = user.uid
        val usernameLower = username.trim().lowercase(Locale.ROOT)

        // transazione: prenota username + crea profilo
        fs.runTransaction { tx ->
            val unameDoc = fds.usernamesDoc(usernameLower)
            if (tx.get(unameDoc).exists()) throw IllegalStateException("USERNAME_TAKEN")
            tx.set(unameDoc, mapOf("uid" to uid, "createdAt" to System.currentTimeMillis()))
            val profile = UserProfile(
                uid = uid,
                name = name,
                surname = surname,
                email = email,
                username = usernameLower,
                avatarUrl = null,
                createdAt = System.currentTimeMillis()
            )
            tx.set(fds.userDoc(uid), profile, SetOptions.merge())
            null
        }.await()

        // (facoltativo) verifica email
        runCatching { authDs.sendEmailVerification() }
    }

    suspend fun signInWithEmail(email: String, password: String) =
        authDs.signInEmail(email, password)

    suspend fun signOut() = authDs.signOut()
    suspend fun sendPasswordReset(email: String) = authDs.sendPasswordReset(email)
}