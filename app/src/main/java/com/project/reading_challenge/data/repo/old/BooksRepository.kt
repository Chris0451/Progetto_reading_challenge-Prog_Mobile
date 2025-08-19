package com.project.reading_challenge.data.repo.old

import com.google.firebase.firestore.Query
import com.project.reading_challenge.data.remote.FirebaseAuthDataSource
import com.project.reading_challenge.data.remote.FirestoreDataSource
import com.project.reading_challenge.data.remote.GoogleBooksApi
import com.project.reading_challenge.data.remote.VolumeItem
import com.project.reading_challenge.domain.model.BookSnapshot
import com.project.reading_challenge.domain.model.ReadingStatus
import com.project.reading_challenge.domain.model.UserBook
import kotlinx.coroutines.flow.Flow

class BooksRepository(
    private val authDs: FirebaseAuthDataSource,
    private val fds: FirestoreDataSource,
    private val booksApi: GoogleBooksApi,
    private val apiKey: String
) {
    private fun requireUid() = authDs.currentUid() ?: error("Not signed in")

    // --- Google Books ---
    suspend fun searchBooks(q: String, page: Int, pageSize: Int) =
        booksApi.searchVolumes(q = q, startIndex = page * pageSize, maxResults = pageSize, key = apiKey)

    suspend fun getVolume(volumeId: String) =
        booksApi.getVolume(volumeId, key = apiKey)

    fun toSnapshot(volume: VolumeItem): BookSnapshot =
        BookSnapshot(
            title = volume.volumeInfo?.title.orEmpty(),
            authors = volume.volumeInfo?.authors ?: emptyList(),
            thumbnail = volume.volumeInfo?.imageLinks?.thumbnail?.replace("http://", "https://"),
            categories = volume.volumeInfo?.categories ?: emptyList(),
            pageCount = volume.volumeInfo?.pageCount,
            publishedDate = volume.volumeInfo?.publishedDate
        )

    // --- Firestore: libreria personale ---
    suspend fun addGoogleBook(volumeId: String, snapshot: BookSnapshot?) {
        val uid = requireUid()
        val col = fds.userBooks(uid)
        val doc = col.document()
        val entry = UserBook(
            id = doc.id,
            volumeId = volumeId,
            snapshot = snapshot,
            updatedAt = System.currentTimeMillis()
        )
        fds.set(doc, entry)
    }

    suspend fun updateStatus(bookId: String, status: ReadingStatus) {
        val uid = requireUid()
        val doc = fds.userBooks(uid).document(bookId)
        fds.set(doc, mapOf("status" to status, "updatedAt" to System.currentTimeMillis()))
    }

    suspend fun setShelves(bookId: String, shelfIds: List<String>) {
        val uid = requireUid()
        val doc = fds.userBooks(uid).document(bookId)
        fds.set(doc, mapOf("shelfIds" to shelfIds, "updatedAt" to System.currentTimeMillis()))
    }

    suspend fun deleteBook(bookId: String) {
        val uid = requireUid()
        fds.delete(fds.userBooks(uid).document(bookId))
    }

    fun observeMyBooks(): Flow<List<UserBook>> {
        val uid = requireUid()
        val q = fds.userBooks(uid).orderBy("updatedAt", Query.Direction.DESCENDING)
        return fds.observeList(q, UserBook::class.java) { i, id, item -> item.copy(id = id) }
    }
}