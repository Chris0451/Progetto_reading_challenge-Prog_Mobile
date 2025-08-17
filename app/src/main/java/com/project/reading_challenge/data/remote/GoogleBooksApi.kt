package com.project.reading_challenge.data.remote

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

// DTO minimal per Google Books
data class VolumesResponse(val items: List<VolumeItem> = emptyList(), val totalItems: Int = 0)
data class VolumeItem(val id: String, val volumeInfo: VolumeInfo?)
data class VolumeInfo(
    val title: String? = null,
    val authors: List<String>? = null,
    val description: String? = null,
    val categories: List<String>? = null,
    val imageLinks: ImageLinks? = null,
    val pageCount: Int? = null,
    val publishedDate: String? = null,
    val language: String? = null
)
data class ImageLinks(val thumbnail: String? = null, val smallThumbnail: String? = null)

interface GoogleBooksApi {
    @GET("volumes")
    suspend fun searchVolumes(
        @Query("q") q: String,
        @Query("startIndex") startIndex: Int = 0,
        @Query("maxResults") maxResults: Int = 20,
        @Query("orderBy") orderBy: String? = null,        // relevance | newest
        @Query("langRestrict") lang: String? = "it",
        @Query("printType") printType: String? = "books",
        @Query("key") key: String
    ): VolumesResponse

    @GET("volumes/{id}")
    suspend fun getVolume(
        @Path("id") id: String,
        @Query("key") key: String
    ): VolumeItem
}