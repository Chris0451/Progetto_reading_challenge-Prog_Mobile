package com.project.reading_challenge.data.repo

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.project.reading_challenge.domain.model.UserProfile
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import java.util.Locale

class AuthRepository (
    private val auth: FirebaseAuth,
    private val db: FirebaseFirestore
){
    fun authState(): Flow<String?> = callbackFlow {
        val listener = FirebaseAuth.AuthStateListener { firebaseAuth ->
            trySend(firebaseAuth.currentUser?.uid)
        }
        auth.addAuthStateListener(listener)
        awaitClose { auth.removeAuthStateListener(listener) }
    }

    suspend fun signUpWithEmail(
        email: String,
        password: String,
        name: String?,
        surname: String?,
        username: String
    ) {
        // 1) crea account email/password
        val result = auth.createUserWithEmailAndPassword(email, password).await()
        val user = result.user ?: error("User creation failed")

        // 2) transazione per riservare username e creare profilo
        val uid = user.uid
        val usernameLower = username.trim().lowercase(Locale.ROOT)
        val usernamesDoc = db.collection("usernames").document(usernameLower)
        val userDoc = db.collection("users").document(uid)

        try {
            db.runTransaction { tx ->
                val snap = tx.get(usernamesDoc)
                if (snap.exists()) {
                    throw IllegalStateException("USERNAME_TAKEN")
                }
                tx.set(usernamesDoc, mapOf("uid" to uid, "createdAt" to System.currentTimeMillis()))
                val profile = UserProfile(
                    uid = uid,
                    name = name,
                    surname = surname,
                    email = email,
                    username = usernameLower,
                    avatarUrl = null,
                    createdAt = System.currentTimeMillis()
                )
                tx.set(userDoc, profile, SetOptions.merge())
                null
            }.await()
        } catch (e: Exception) {
            // username occupato → rollback: elimina utente auth
            user.delete().await()
            throw e
        }

        // 3) aggiorna displayName in Auth (facoltativo)
        val display = listOfNotNull(name, surname).joinToString(" ").ifBlank { username }
        user.updateProfile(UserProfileChangeRequest.Builder().setDisplayName(display).build()).await()

        // 4) invia verifica email (consigliato)
        runCatching { user.sendEmailVerification().await() }
    }

    suspend fun signInWithEmail(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password).await()
    }

    suspend fun signOut() {
        auth.signOut()
    }

    suspend fun sendPasswordReset(email: String) {
        auth.sendPasswordResetEmail(email).await()
    }

    fun currentUid(): String? = auth.currentUser?.uid
}