package com.mohitb117.demo_omdb_api.injection

import android.content.Context
import com.mohitb117.demo_omdb_api.dao.FavouritesDao
import com.mohitb117.demo_omdb_api.databases.MovieResultsDb
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DbModule {

    @Provides
    @Singleton
    fun provideDb(@ApplicationContext context: Context): MovieResultsDb {
        return MovieResultsDb.getDatabase(context)
    }

    @Provides
    @Singleton
    fun provideFavouritesDao(movieResultsDb: MovieResultsDb): FavouritesDao {
        return movieResultsDb.favouritesDao()
    }
}