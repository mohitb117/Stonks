package com.mohitb117.stonks.ui.stocks

import android.util.Log
import androidx.lifecycle.*
import com.mohitb117.stonks.datamodels.Portfolio
import com.mohitb117.stonks.common.ResultWrapper
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
@Inject constructor(
    private val repository: StonksRepository,
) : ViewModel() {

    private var job: Job? = null

    private val _portfolio = MutableLiveData<ResultWrapper<Portfolio>>(null)
    val portfolio: LiveData<ResultWrapper<Portfolio>> = _portfolio

    fun getCachedPortfolio() =
        repository.getCachedPortfolio().map { ResultWrapper.Success(it, true) }

    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        Log.e(TAG, "Error Encountered: ${throwable.localizedMessage}", throwable)
        _portfolio.postValue(ResultWrapper.Error(throwable))
    }

    fun loadStocks() {
        _portfolio.postValue(ResultWrapper.Loading())

        job = viewModelScope.launch(Dispatchers.IO + exceptionHandler) {

            val result = when (val result = repository.loadPortfolio()) {
                is ApiResult.Success -> {
                    val portfolio = Portfolio(result.value.stocks)

                    repository.insertAll(portfolio.stocks)

                    ResultWrapper.Success(portfolio)
                }

                is ApiResult.Failure -> when (result) {
                    is ApiResult.Failure.ApiFailure -> ResultWrapper.Error(result.error)
                    is ApiResult.Failure.HttpFailure -> ResultWrapper.Error(result.error)
                    is ApiResult.Failure.NetworkFailure -> ResultWrapper.Error(result.error)
                    is ApiResult.Failure.UnknownFailure -> ResultWrapper.Error(result.error)
                    else -> ResultWrapper.Error("Not sure what is going on!!! 🙈🥺 ")
                }
            }

            _portfolio.postValue(result)
        }
    }

    override fun onCleared() {
        job?.cancel()
        super.onCleared()
    }


    companion object {
        private val TAG = StonksViewModel::class.java.simpleName
    }
}