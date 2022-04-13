package com.mohitb117.stonks.activities

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mohitb117.stonks.datamodels.Stock
import com.mohitb117.stonks.repositories.PortfolioEndpoint
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LaunchActivityViewModel
@Inject constructor() : ViewModel() {
    
    private val _portfolioEndpoint = MutableLiveData<PortfolioEndpoint>(PortfolioEndpoint.Good)
    val portfolioEndpoint = _portfolioEndpoint
    
    private val _stockSelectedEvent= MutableLiveData<Stock>()
    val stockSelectedEvent = _stockSelectedEvent

    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        Log.e(TAG, "Error Encountered: ${throwable.localizedMessage}", throwable)
    }

    fun onGoodPortfolioEndpointSelected() =
        viewModelScope.launch(exceptionHandler) { selectPortfolioEndpoint(PortfolioEndpoint.Good) }

    fun onEmptyEndpointSelected() =
        viewModelScope.launch(exceptionHandler) { selectPortfolioEndpoint(PortfolioEndpoint.Empty) }

    fun onMalformedPortfolioEndpointSelected() =
        viewModelScope.launch(exceptionHandler) { selectPortfolioEndpoint(PortfolioEndpoint.Malformed) }

    fun onStockSelected(stock: Stock) {
        _stockSelectedEvent.value = stock
    }

    private fun selectPortfolioEndpoint(portfolioEndpoint: PortfolioEndpoint) {
        _portfolioEndpoint.value = portfolioEndpoint
    }

    companion object {
        private val TAG = LaunchActivityViewModel::class.java.simpleName
    }
}
