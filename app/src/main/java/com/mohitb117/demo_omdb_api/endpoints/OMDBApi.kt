package com.mohitb117.demo_omdb_api.endpoints

import com.mohitb117.demo_omdb_api.datamodels.DetailsResultsBody
import com.mohitb117.demo_omdb_api.datamodels.SearchResultsBody
import com.mohitb117.demo_omdb_api.injection.NetworkingModule.Companion.OMDB_BASE_URL
import com.slack.eithernet.ApiResult
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface OMDBApi {
    @GET(OMDB_BASE_URL)
    suspend fun loadResults(
        @Query("apikey") key: String,
        @Query("s") term: String
    ): ApiResult<SearchResultsBody, String>

    @GET(OMDB_BASE_URL)
    suspend fun loadDetails(
        @Query("apikey") key: String,
        @Query("i") imdbId: String
    ): ApiResult<DetailsResultsBody, String>
}