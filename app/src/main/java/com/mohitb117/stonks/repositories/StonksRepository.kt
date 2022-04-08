package com.mohitb117.stonks.repositories

import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import com.mohitb117.stonks.dao.CachedPortfolioDao
import com.mohitb117.stonks.datamodels.Portfolio
import com.mohitb117.stonks.datamodels.Stock
import com.mohitb117.stonks.endpoints.Api
import javax.inject.Inject

class StonksRepository
@Inject constructor(
    private val endpoint: Api,
    private val cachedPortfolioDao: CachedPortfolioDao,
) {

    fun getCachedPortfolio(): LiveData<Portfolio> = cachedPortfolioDao.getStocks().map { Portfolio(it) }

    suspend fun contains(ticker: String): Boolean {
        return cachedPortfolioDao.getForTicker(ticker).isNotEmpty()
    }

    suspend fun insert(stock: Stock) {
        cachedPortfolioDao.insert(stock)
    }

    suspend fun insertAll(stocks: List<Stock>) {
        cachedPortfolioDao.insertAll(stocks)
    }

    suspend fun delete(ticker: String) {
        cachedPortfolioDao.delete(ticker)
    }

    suspend fun loadPortfolio() = endpoint.loadPortfolioGood()

    suspend fun loadDetails(ticker: String) =
        endpoint.loadDetails(ticker = ticker, apiKey = DETAILS_API_KEY)

    companion object {
        private const val DETAILS_API_KEY = "QmdiNBsdgpeF3jIvBXAGFnQw7qvwzZvp"
    }
}