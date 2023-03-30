package com.jkhanh.movielist.di

import com.jkhanh.movielist.data.repository.MovieRepository
import com.jkhanh.movielist.data.repository.MovieRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Singleton
    @Binds
    abstract fun bindProductRepository(impl: MovieRepositoryImpl): MovieRepository
}