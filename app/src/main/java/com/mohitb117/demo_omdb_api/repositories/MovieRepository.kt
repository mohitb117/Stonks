package com.mohitb117.demo_omdb_api.repositories

import com.mohitb117.demo_omdb_api.dao.FavouritesDao
import com.mohitb117.demo_omdb_api.datamodels.DetailsResultsBody
import com.mohitb117.demo_omdb_api.datamodels.SearchResult
import com.mohitb117.demo_omdb_api.datamodels.SearchResultsBody
import com.mohitb117.demo_omdb_api.endpoints.OMDBApi
import com.slack.eithernet.ApiResult
import retrofit2.Response
import javax.inject.Inject

class MovieRepository
@Inject constructor(
    private val endpoint: OMDBApi,
    private val favouritesDao: FavouritesDao,
) {

    fun getFavItems() = favouritesDao.getAllFavItems()

    suspend fun isFavourite(imdbId: String): Boolean {
        return favouritesDao.getForId(imdbId).isNotEmpty()
    }

    suspend fun insert(searchResult: SearchResult) {
        favouritesDao.insert(searchResult)
    }

    suspend fun delete(imdbId: String) {
        favouritesDao.delete(imdbId)
    }

    suspend fun loadResults(term: String) =
        endpoint.loadResults(API_KEY, term)

    suspend fun loadDetails(imdbId: String) =
        endpoint.loadDetails(API_KEY, imdbId)

    companion object {
        private const val API_KEY = "4f99d5d"
    }
}