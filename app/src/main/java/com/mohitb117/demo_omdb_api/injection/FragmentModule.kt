package com.mohitb117.demo_omdb_api.injection

import com.mohitb117.demo_omdb_api.adapters.SearchListAdapter
import com.mohitb117.demo_omdb_api.adapters.VideosDiffCallback
import com.mohitb117.demo_omdb_api.dao.FavouritesDao
import com.mohitb117.demo_omdb_api.repositories.MovieRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.FragmentComponent
import dagger.hilt.android.scopes.FragmentScoped

@Module
@InstallIn(FragmentComponent::class)
class FragmentModule {

    @Provides
    @FragmentScoped
    fun provideListAdapter(
        repository: MovieRepository,
    ): SearchListAdapter {
        return SearchListAdapter(VideosDiffCallback(), repository)
    }
}