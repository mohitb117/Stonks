package com.mohitb117.stonks.endpoints

import com.mohitb117.stonks.datamodels.StockDetails
import com.mohitb117.stonks.injection.NetworkingModule.Companion.DETAILS_BASE_URL
import com.slack.eithernet.ApiResult
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface DetailsApi {
    @GET(DETAILS_BASE_URL)
    suspend fun loadDetails(
        @Path("ticker") ticker: String,
        @Query("apiKey") apiKey: String,
        @Query("adjusted") adjusted: String,
    ): ApiResult<StockDetails, String>
}