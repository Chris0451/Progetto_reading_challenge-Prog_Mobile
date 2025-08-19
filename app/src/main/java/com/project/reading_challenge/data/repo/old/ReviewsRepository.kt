package com.project.reading_challenge.data.repo.old

import com.google.firebase.firestore.Query
import com.project.reading_challenge.data.remote.FirebaseAuthDataSource
import com.project.reading_challenge.data.remote.FirestoreDataSource
import com.project.reading_challenge.domain.model.Review
import kotlinx.coroutines.flow.Flow

class ReviewsRepository(
    private val authDs: FirebaseAuthDataSource,
    private val fds: FirestoreDataSource
) {
    private fun requireUid() = authDs.currentUid() ?: error("Not signed in")

    suspend fun addReview(volumeId: String, rating: Int, text: String) {
        val uid = requireUid()
        val col = fds.reviewsCol(volumeId)
        val doc = col.document()
        val now = System.currentTimeMillis()
        val review = Review(
            id = doc.id, volumeId = volumeId, authorUid = uid,
            rating = rating, text = text, createdAt = now, updatedAt = now
        )
        fds.set(doc, review, merge = false)
    }

    suspend fun updateReview(volumeId: String, reviewId: String, rating: Int, text: String) {
        val uid = requireUid()
        val doc = fds.reviewsCol(volumeId).document(reviewId)
        fds.set(doc, mapOf(
            "authorUid" to uid, // per sicurezza lato regole
            "rating" to rating,
            "text" to text,
            "updatedAt" to System.currentTimeMillis()
        ))
    }

    suspend fun deleteReview(volumeId: String, reviewId: String) {
        fds.delete(fds.reviewsCol(volumeId).document(reviewId))
    }

    fun observeReviews(volumeId: String): Flow<List<Review>> {
        val q = fds.reviewsCol(volumeId).orderBy("createdAt", Query.Direction.DESCENDING)
        return fds.observeList(q, Review::class.java) { _, id, item -> item.copy(id = id, volumeId = volumeId) }
    }
}