package com.mohitb117.demo_omdb_api.injection

import android.content.Context
import com.mohitb117.demo_omdb_api.dao.FavouritesDao
import com.mohitb117.demo_omdb_api.databases.MovieResultsDb
import com.mohitb117.demo_omdb_api.endpoints.OMDBApi
import com.mohitb117.demo_omdb_api.repositories.MovieRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class RepositoryModule {

    @Provides
    @Singleton
    fun provideRepository(
        omdbApi: OMDBApi,
        favouritesDao: FavouritesDao
    ): MovieRepository {
        return MovieRepository(omdbApi, favouritesDao)
    }
}
