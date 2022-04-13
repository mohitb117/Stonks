package com.mohitb117.stonks.activities

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mohitb117.stonks.datamodels.Stock
import com.mohitb117.stonks.repositories.PortfolioEndpoint
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LaunchActivityViewModel
@Inject constructor() : ViewModel() {
    
    private val _portfolioEndpoint = MutableLiveData<PortfolioEndpoint>(PortfolioEndpoint.Good)
    val portfolioEndpoint = _portfolioEndpoint
    
    private val _stockSelected= MutableLiveData<Stock>()
    val stockSelected = _stockSelected

    fun onGoodPortfolioEndpointSelected() {
        selectPortfolioEndpoint(PortfolioEndpoint.Good)
    }

    fun onEmptyEndpointSelected() {
        selectPortfolioEndpoint(PortfolioEndpoint.Empty)
    }

    fun onMalformedPortfolioEndpointSelected() {
        selectPortfolioEndpoint(PortfolioEndpoint.Malformed)
    }

    fun onStockSelected(stock: Stock) {
        _stockSelected.value = stock
    }

    private fun selectPortfolioEndpoint(portfolioEndpoint: PortfolioEndpoint) {
        _portfolioEndpoint.value = portfolioEndpoint
    }

    companion object {
        private val TAG = LaunchActivityViewModel::class.java.simpleName
    }
}
