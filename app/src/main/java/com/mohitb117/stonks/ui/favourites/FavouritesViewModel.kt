package com.mohitb117.stonks.ui.favourites

import androidx.lifecycle.ViewModel
import com.mohitb117.stonks.repositories.StonksRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * ViewModel responsible for fetching / loading results from cached data from STONKS on RoSTONKS.
 */
@HiltViewModel
class FavouritesViewModel
@Inject constructor(
    private val repository: StonksRepository
) : ViewModel() {
    fun getFavItems() = repository.getCachedPortfolio()
}