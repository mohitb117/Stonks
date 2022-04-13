package com.mohitb117.stonks

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.mohitb117.stonks.activities.LaunchActivityViewModel
import com.mohitb117.stonks.datamodels.Stock
import com.mohitb117.stonks.repositories.PortfolioEndpoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(MockitoJUnitRunner::class)
class LaunchingActivityViewModelTests {

    private lateinit var viewModel: LaunchActivityViewModel

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val coroutineTestRule = CoroutineTestRule()

    @Mock
    private lateinit var portfolioEndpointObserver: Observer<PortfolioEndpoint>

    @Mock
    private lateinit var stockSelectedObserver: Observer<Stock>

    @Before
    fun before() {
        MockitoAnnotations.openMocks(this)

        viewModel = LaunchActivityViewModel()

        viewModel.apply {
            portfolioEndpoint.observeForever(portfolioEndpointObserver)
            stockSelected.observeForever(stockSelectedObserver)
        }
    }

    @After
    fun after() {
        viewModel.apply {
            portfolioEndpoint.removeObserver(portfolioEndpointObserver)
            stockSelected.removeObserver(stockSelectedObserver)
        }
    }

    @Test
    fun testNoPortfolioSelectedLaunchesGood() {
        // Arrange

        // Act

        // Assert (it launches twice)
        verify(portfolioEndpointObserver).onChanged(PortfolioEndpoint.Good)
    }

    @Test
    fun testOnGoodPortfolioSelected() {
        // Arrange

        // Act
        viewModel.onGoodPortfolioEndpointSelected()

        // Assert (it launches twice)
        verify(portfolioEndpointObserver, times(2)).onChanged(PortfolioEndpoint.Good)
    }

    @Test
    fun testOnMalformedPortfolioSelected() {
        // Arrange

        // Act
        viewModel.onMalformedPortfolioEndpointSelected()

        // Assert

        verify(portfolioEndpointObserver).onChanged(PortfolioEndpoint.Malformed)
    }

    @Test
    fun testOnEmptyPortfolioSelected() {
        // Arrange

        // Act
        viewModel.onEmptyEndpointSelected()

        // Assert
        verify(portfolioEndpointObserver).onChanged(PortfolioEndpoint.Empty)
    }

    @Test
    fun testOnStockSelected() {
        // Arrange
        val stock = Stock(
            currency = "",
            current_price_cents = null,
            current_price_timestamp = 0,
            name = "",
            quantity = null,
            ticker = "",
            ratio = 0.0f
        )
        // Act
        viewModel.onStockSelected(stock)

        // Assert
        verify(stockSelectedObserver).onChanged(stock)
    }
}