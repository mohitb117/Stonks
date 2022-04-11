package com.mohitb117.stonks.endpoints

import com.mohitb117.stonks.datamodels.Portfolio
import com.mohitb117.stonks.injection.NetworkingModule.Companion.STONKS_BASE_URL
import com.slack.eithernet.ApiResult
import retrofit2.http.GET

interface ListApi {
    @GET("${STONKS_BASE_URL}portfolio.json")
    suspend fun loadPortfolioGood(
    ): ApiResult<Portfolio, String>

    @GET("${STONKS_BASE_URL}portfolio_malformed.json")
    suspend fun loadPortfolioMalformed(
    ): ApiResult<Portfolio, String>

    @GET("${STONKS_BASE_URL}portfolio_empty.json")
    suspend fun loadPortfolioBlank(
    ): ApiResult<Portfolio, String>
}