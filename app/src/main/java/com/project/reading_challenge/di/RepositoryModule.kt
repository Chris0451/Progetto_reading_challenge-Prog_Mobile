package com.project.reading_challenge.di

import com.google.firebase.firestore.FirebaseFirestore
import com.project.reading_challenge.BuildConfig
import com.project.reading_challenge.data.remote.FirebaseAuthDataSource
import com.project.reading_challenge.data.remote.FirestoreDataSource
import com.project.reading_challenge.data.remote.GoogleBooksApi
import com.project.reading_challenge.data.repo.old.AuthRepository
import com.project.reading_challenge.data.repo.old.BooksRepository
import com.project.reading_challenge.data.repo.old.CatalogRepository
import com.project.reading_challenge.data.repo.old.ReviewsRepository
import com.project.reading_challenge.data.repo.old.UserPreferencesRepository
import com.project.reading_challenge.data.repo.old.UserRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {
    @Provides @Singleton
    fun provideAuthRepository(
        authDs: FirebaseAuthDataSource,
        fs: FirebaseFirestore,
        fds: FirestoreDataSource
    ) = AuthRepository(authDs, fs, fds)

    @Provides @Singleton
    fun provideUserRepository(
        authDs: FirebaseAuthDataSource,
        fds: FirestoreDataSource
    ) = UserRepository(authDs, fds)

    @Provides @Singleton @Named("BOOKS_API_KEY")
    fun provideBooksApiKey(): String = BuildConfig.BOOKS_API_KEY

    @Provides @Singleton
    fun provideBooksRepository(
        authDs: FirebaseAuthDataSource,
        fds: FirestoreDataSource,
        api: GoogleBooksApi,
        @Named("BOOKS_API_KEY") apiKey: String
    ): BooksRepository = BooksRepository(
        authDs = authDs,
        fds = fds,
        booksApi = api,
        apiKey = apiKey
    )

    @Provides @Singleton
    fun provideReviewsRepository(
        authDs: FirebaseAuthDataSource,
        fds: FirestoreDataSource
    ) = ReviewsRepository(authDs, fds)

    @Provides @Singleton
    fun provideUserPreferencesRepository(
        authDs: FirebaseAuthDataSource,
        fds: FirestoreDataSource
    ) = UserPreferencesRepository(authDs, fds)

    @Provides @Singleton
    fun provideCatalogRepository(
        api: GoogleBooksApi,
        fds: FirestoreDataSource
    ) = CatalogRepository(
        api = api,
        apiKey = BuildConfig.BOOKS_API_KEY, // assicurati del buildConfigField
        fds = fds
    )
}