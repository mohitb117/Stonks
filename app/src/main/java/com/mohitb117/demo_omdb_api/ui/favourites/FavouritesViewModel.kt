package com.mohitb117.demo_omdb_api.ui.favourites

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mohitb117.demo_omdb_api.dao.FavouritesDao
import com.mohitb117.demo_omdb_api.repositories.MovieRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * ViewModel responsible for fetching / loading results from cached data from OMDB on RoomDB.
 */
@HiltViewModel
class FavouritesViewModel
@Inject constructor(
    private val repository: MovieRepository
) : ViewModel() {
    fun getFavItems() = repository.getFavItems()
}