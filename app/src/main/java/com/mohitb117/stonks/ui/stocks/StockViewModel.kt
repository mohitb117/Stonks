package com.mohitb117.stonks.ui.stocks

import android.util.Log
import androidx.annotation.VisibleForTesting
import androidx.lifecycle.*
import com.mohitb117.stonks.datamodels.Portfolio
import com.mohitb117.stonks.common.ResultWrapper
import com.mohitb117.stonks.repositories.PortfolioEndpoint
import com.mohitb117.stonks.repositories.StonksRepository
import com.slack.eithernet.ApiResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import javax.inject.Inject

/**
 * ViewModel responsible for fetching / loading results from STONKS.
 */
@HiltViewModel
class StonksViewModel
@VisibleForTesting
constructor(
    private val repository: StonksRepository,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : ViewModel() {

    @Inject
    constructor(repository: StonksRepository) : this(repository, Dispatchers.IO)

    private var job: Job? = null

    private val _portfolio = MutableLiveData<ResultWrapper<Portfolio>>(null)
    val portfolio: LiveData<ResultWrapper<Portfolio>> = _portfolio

    fun getCachedPortfolio() =
        repository.getCachedPortfolio().map { ResultWrapper.Success(it, true) }

    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        Log.e(TAG, "Error Encountered: ${throwable.localizedMessage}", throwable)
        _portfolio.postValue(ResultWrapper.Error(throwable))
    }

    fun loadStocks(portfolioEndpoint: PortfolioEndpoint) {
        job = viewModelScope.launch(ioDispatcher + exceptionHandler) {
            loadStocksInternal(portfolioEndpoint)
        }
    }

    @VisibleForTesting
    suspend fun loadStocksInternal(portfolioEndpoint: PortfolioEndpoint) {
        _portfolio.postValue(ResultWrapper.Loading())

        val result = repository.loadPortfolio(portfolioEndpoint)

        _portfolio.postValue(result)
    }

    override fun onCleared() {
        job?.cancel()
        super.onCleared()
    }


    companion object {
        private val TAG = StonksViewModel::class.java.simpleName
    }
}