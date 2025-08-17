package com.project.reading_challenge.data.remote

import com.google.firebase.firestore.DocumentReference
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

inline fun <reified T> observeDoc(ref: DocumentReference): Flow<T?> = callbackFlow {
    val reg = ref.addSnapshotListener { snap, _ ->
        trySend(snap?.toObject(T::class.java))
    }
    awaitClose { reg.remove() }
}
