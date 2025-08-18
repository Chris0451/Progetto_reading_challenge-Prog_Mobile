package com.project.reading_challenge.data.repo

import com.project.reading_challenge.data.remote.FirestoreDataSource
import com.project.reading_challenge.data.remote.GoogleBooksApi
import com.project.reading_challenge.data.remote.VolumeItem
import com.project.reading_challenge.domain.model.BookSnapshot
import com.project.reading_challenge.domain.model.UserPreferences
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CatalogRepository @Inject constructor(
    private val api: GoogleBooksApi,
    private val apiKey: String,
    private val fds: FirestoreDataSource,
) {
    // Strategia: query a Google Books per ciascuna categoria preferita.
    // Esempio di q: subject:Fantasy  (usa langPreferita dove possibile)
    suspend fun fetchRecommendations(prefs: UserPreferences, pageSize: Int = 12): List<VolumeItem> {
        if (prefs.categories.isEmpty()) return emptyList()
        val results = mutableListOf<VolumeItem>()
        for (cat in prefs.categories) {
            val q = "subject:${cat.trim()}"
            val res = api.searchVolumes(
                q = q,
                startIndex = 0,
                maxResults = pageSize,
                orderBy = "relevance",
                lang = prefs.language,
                key = apiKey
            )
            results += res.items
        }
        // opzionale: deduplica per id
        return results.distinctBy { it.id }
    }

    // Cache pubblica opzionale per categorie curate manualmente (admin)
    // /catalog_curated/{category}/items/{volumeId} -> { volumeId, title, thumbnail, ... }
    data class CuratedItem(
        val volumeId: String = "",
        val title: String = "",
        val thumbnail: String? = null,
        val category: String = ""
    )

    fun observeCurated(category: String): Flow<List<CuratedItem>> {
        val q = fds.publicBookDoc("dummy") // solo per riuso oggetti; ignorato
        val col = fds.publicBookDoc("dummy").parent // root
            .document("catalog_curated").collection(category)
        return fds.observeList(
            query = col,
            clazz = CuratedItem::class.java
        )
    }

    fun toSnapshot(item: VolumeItem): BookSnapshot =
        BookSnapshot(
            title = item.volumeInfo?.title.orEmpty(),
            authors = item.volumeInfo?.authors ?: emptyList(),
            thumbnail = item.volumeInfo?.imageLinks?.thumbnail?.replace("http://", "https://"),
            categories = item.volumeInfo?.categories ?: emptyList(),
            pageCount = item.volumeInfo?.pageCount,
            publishedDate = item.volumeInfo?.publishedDate
        )

    suspend fun fetchRecommendations(prefs: UserPreferences): List<VolumeItem> {
        // Implementazione reale con Google Books + preferenze
        return emptyList()
    }
}