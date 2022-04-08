package com.mohitb117.stonks.endpoints

import com.mohitb117.stonks.datamodels.Details
import com.mohitb117.stonks.datamodels.Portfolio
import com.mohitb117.stonks.injection.NetworkingModule.Companion.DETAILS_BASE_URL
import com.mohitb117.stonks.injection.NetworkingModule.Companion.STONKS_BASE_URL
import com.slack.eithernet.ApiResult
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface Api {
    @GET("${STONKS_BASE_URL}portfolio.json")
    suspend fun loadPortfolioGood(
    ): ApiResult<Portfolio, String>

    @GET("${STONKS_BASE_URL}portfolio_malformed.json")
    suspend fun loadPortfolioMalformed(
    ): ApiResult<Portfolio, String>

    @GET("${STONKS_BASE_URL}portfolio_empty.json")
    suspend fun loadPortfolioBlank(
    ): ApiResult<Portfolio, String>

    @GET(DETAILS_BASE_URL)
    suspend fun loadDetails(
        @Path("ticker") ticker: String,
        @Query("api_key") apiKey: String
    ): ApiResult<Details, String>
}