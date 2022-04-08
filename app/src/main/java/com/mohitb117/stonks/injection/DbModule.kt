package com.mohitb117.stonks.injection

import android.content.Context
import com.mohitb117.stonks.dao.CachedPortfolioDao
import com.mohitb117.stonks.databases.PortfolioDb
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
    fun provideDb(@ApplicationContext context: Context): PortfolioDb {
        return PortfolioDb.getDatabase(context)
    }

    @Provides
    @Singleton
    fun provideFavouritesDao(portfolioDb: PortfolioDb): CachedPortfolioDao {
        return portfolioDb.favouritesDao()
    }
}