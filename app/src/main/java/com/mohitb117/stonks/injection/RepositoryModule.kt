package com.mohitb117.stonks.injection

import com.mohitb117.stonks.dao.CachedPortfolioDao
import com.mohitb117.stonks.endpoints.Api
import com.mohitb117.stonks.repositories.StonksRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class RepositoryModule {

    @Provides
    @Singleton
    fun provideRepository(
        STONKSApi: Api,
        cachedPortfolioDao: CachedPortfolioDao
    ): StonksRepository {
        return StonksRepository(STONKSApi, cachedPortfolioDao)
    }
}
