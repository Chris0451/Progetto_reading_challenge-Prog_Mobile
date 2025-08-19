package com.project.reading_challenge.data.repo.old

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

        val userDoc = db.collection("users").document(uid)
        userDoc.set(UserProfile(uid = uid)).await()

        val shelfRef = userDoc.collection("shelves").document()
        shelfRef.set(Shelf(id = shelfRef.id, name = "Da leggere", isDefault = true)).await()

        val bookRef = userDoc.collection("books").document()
        bookRef.set(
            UserBook(
                id = bookRef.id,
                volumeId = "zyTCAlFPjgYC",
                status = ReadingStatus.TO_READ,
                shelfIds = listOf(shelfRef.id)
            )
        ).await()

        val reviewRef = db.collection("books").document("zyTCAlFPjgYC")
            .collection("reviews").document()
        reviewRef.set(
            Review(
                id = reviewRef.id,
                volumeId = "zyTCAlFPjgYC",
                authorUid = uid,
                rating = 5,
                text = "Ottimo inizio!"
            )
        ).await()
    }
}