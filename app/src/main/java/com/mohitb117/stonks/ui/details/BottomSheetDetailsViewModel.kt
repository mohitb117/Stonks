package com.mohitb117.stonks.ui.details

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mohitb117.stonks.common.ResultWrapper
import com.mohitb117.stonks.datamodels.Stock
import com.mohitb117.stonks.datamodels.StockDetails
import com.mohitb117.stonks.repositories.StonksRepository
import com.slack.eithernet.ApiResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel responsible for fetching detailed results from from STONKS api.
 */
@HiltViewModel
class BottomSheetDetailsViewModel
@Inject constructor(
    private val repository: StonksRepository
) : ViewModel() {

    private var job: Job? = null

    private val _portfolioDetails = MutableLiveData<ResultWrapper<StockDetails>?>()
    val portfolioDetails: LiveData<ResultWrapper<StockDetails>?> = _portfolioDetails

    fun loadDetailsResult(stock: Stock) {
        _portfolioDetails.postValue(ResultWrapper.Loading())

        val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
            Log.e(TAG, "Error Encountered: ${throwable.localizedMessage}")
            _portfolioDetails.postValue(ResultWrapper.Error(throwable))
        }

        job = viewModelScope.launch(Dispatchers.IO + exceptionHandler) {
            val result = when (val result = repository.loadStockDetails(stock.ticker)) {
                is ApiResult.Success -> ResultWrapper.Success(result.value)

                is ApiResult.Failure -> when (result) {
                    is ApiResult.Failure.ApiFailure -> ResultWrapper.Error(result.error)
                    is ApiResult.Failure.HttpFailure -> ResultWrapper.Error("${result.error} & error-code:${result.code}")
                    is ApiResult.Failure.NetworkFailure -> ResultWrapper.Error(result.error)
                    is ApiResult.Failure.UnknownFailure -> ResultWrapper.Error(result.error)
                    else -> ResultWrapper.Error("Not sure what is going on!!! 🙈🥺 ")
                }
            }

            _portfolioDetails.postValue(result)
        }
    }

    override fun onCleared() {
        job?.cancel()
        super.onCleared()
    }

    companion object {
        private val TAG = BottomSheetDetailsViewModel::class.java.simpleName
    }
}