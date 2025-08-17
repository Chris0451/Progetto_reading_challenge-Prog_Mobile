package com.project.reading_challenge.data.repo

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.project.reading_challenge.domain.model.*
import kotlinx.coroutines.tasks.await

class FirestoreBootstrap(
    private val auth: FirebaseAuth,
    private val db: FirebaseFirestore
) {
    suspend fun ensureUserAndSampleData() {
        val uid = auth.currentUser?.uid ?: auth.signInAnonymously().await().user!!.uid

        // users/{uid} -> UserProfile aggiornato
        val profile = UserProfile(
            uid = uid,
            name = "Mario",
            surname = "Rossi",
            email = auth.currentUser?.email,
            username = "mariorossi",
            avatarUrl = null
        )
        val userDoc = db.collection("users").document(uid)
        userDoc.set(profile).await()

        // esempio shelf di default
        val shelfRef = userDoc.collection("shelves").document()
        shelfRef.set(Shelf(id = shelfRef.id, name = "Da leggere", isDefault = true)).await()
    }
}