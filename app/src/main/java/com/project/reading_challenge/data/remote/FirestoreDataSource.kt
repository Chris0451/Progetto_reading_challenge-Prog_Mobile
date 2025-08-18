package com.project.reading_challenge.data.remote


import com.project.reading_challenge.domain.model.UserPreferences
import com.google.firebase.firestore.*
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FirestoreDataSource @Inject constructor(
    private val db: FirebaseFirestore
) {
    // Helpers path
    fun userDoc(uid: String) = db.collection("users").document(uid)
    fun userBooks(uid: String) = userDoc(uid).collection("books")
    fun userShelves(uid: String) = userDoc(uid).collection("shelves")
    fun userFriends(uid: String) = userDoc(uid).collection("friends")
    fun usernamesDoc(usernameLower: String) = db.collection("usernames").document(usernameLower)

    fun reviewsCol(volumeId: String) = db.collection("books").document(volumeId).collection("reviews")
    fun publicBookDoc(volumeId: String) = db.collection("public_books").document(volumeId)

    suspend fun <T> set(doc: DocumentReference, data: T, merge: Boolean = true) {
        if (merge) doc.set(data as Any, SetOptions.merge()).await()
        else doc.set(data as Any).await()
    }
    suspend fun delete(doc: DocumentReference) = doc.delete().await()

    suspend fun <T> add(col: CollectionReference, data: T): DocumentReference =
        col.add(data as Any).await()

    fun <T : Any> observeList(
        query: Query,
        clazz: Class<T>,
        withDocId: ((index: Int, id: String, item: T) -> T)? = null
    ): Flow<List<T>> = callbackFlow {
        val reg = query.addSnapshotListener { snap, err ->
            if (err != null) { trySend(emptyList()); return@addSnapshotListener }
            val list = snap?.toObjects(clazz)?.mapIndexed { i, item ->
                if (withDocId != null) withDocId(i, snap.documents[i].id, item) else item
            }.orEmpty()
            trySend(list)
        }
        awaitClose { reg.remove() }
    }

    // Adatta il path al tuo schema (users/{uid}/preferences o users/{uid})
    suspend fun getUserPreferences(uid: String): UserPreferences? {
        val snap = db.collection("users")
            .document(uid)
            .collection("meta") // <-- se invece salvi su document "users/{uid}"
            .document("preferences")
            .get()
            .await()

        return snap.toObject(UserPreferences::class.java)
    }
}