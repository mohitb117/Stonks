package com.mohitb117.stonks.repositories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.map
import com.mohitb117.stonks.dao.CachedPortfolioDao
import com.mohitb117.stonks.datamodels.Portfolio
import com.mohitb117.stonks.endpoints.ListApi
import com.mohitb117.stonks.endpoints.DetailsApi
import com.slack.eithernet.ApiResult
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class StonksRepository
@Inject constructor(
    private val listApi: ListApi,
    private val detailsApi: DetailsApi,
    private val cachedPortfolioDao: CachedPortfolioDao,
) {

    fun getCachedPortfolio(): LiveData<Portfolio> =
        cachedPortfolioDao.getStocks().map { Portfolio(it) }

    suspend fun contains(ticker: String): Boolean {
        return cachedPortfolioDao.getForTicker(ticker).isNotEmpty()
    }

    suspend fun insertAll(portfolio: Portfolio) {
        cachedPortfolioDao.insertAll(portfolio.stocks)
    }


    suspend fun loadPortfolio(portfolioEndpoint: PortfolioEndpoint): ApiResult<Portfolio, String> {
        cachedPortfolioDao.deleteAll()

        return when (portfolioEndpoint) {
            PortfolioEndpoint.Empty -> listApi.loadPortfolioBlank()
            PortfolioEndpoint.Good -> listApi.loadPortfolioGood()
            PortfolioEndpoint.Malformed -> listApi.loadPortfolioMalformed()
        }
    }

    suspend fun loadStockDetails(ticker: String) =
        detailsApi.loadDetails(ticker = ticker, apiKey = DETAILS_API_KEY, adjusted = "true")

    companion object {
        private const val DETAILS_API_KEY = "QmdiNBsdgpeF3jIvBXAGFnQw7qvwzZvp"
    }
}

sealed class PortfolioEndpoint {
    object Malformed : PortfolioEndpoint()
    object Good : PortfolioEndpoint()
    object Empty : PortfolioEndpoint()
}