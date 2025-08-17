package com.project.reading_challenge.di

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.project.reading_challenge.data.remote.FirebaseAuthDataSource
import com.project.reading_challenge.data.remote.FirestoreDataSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object FirebaseModule {
    @Provides @Singleton fun provideAuth(): FirebaseAuth = FirebaseAuth.getInstance()
    @Provides @Singleton fun provideFirestore(): FirebaseFirestore = FirebaseFirestore.getInstance()
    @Provides @Singleton fun provideAuthDs(auth: FirebaseAuth) = FirebaseAuthDataSource(auth)
    @Provides @Singleton fun provideFsDs(db: FirebaseFirestore) = FirestoreDataSource(db)
}