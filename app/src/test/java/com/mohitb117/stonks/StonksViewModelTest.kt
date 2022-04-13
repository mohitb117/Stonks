package com.mohitb117.stonks

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.mohitb117.stonks.common.ResultWrapper
import com.mohitb117.stonks.datamodels.Portfolio
import com.mohitb117.stonks.datamodels.Stock
import com.mohitb117.stonks.repositories.PortfolioEndpoint
import com.mohitb117.stonks.repositories.StonksRepository
import com.mohitb117.stonks.ui.stocks.StonksViewModel
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class StonksViewModelTest {

    private lateinit var viewModel: StonksViewModel

    @Mock
    private lateinit var repository: StonksRepository

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val coroutineTestRule = CoroutineTestRule()

    @Mock
    private lateinit var portfolioObserver: Observer<ResultWrapper<Portfolio>>

    @Before
    fun before() {
        viewModel = StonksViewModel(repository, coroutineTestRule.dispatcher)
        viewModel.apply {
            portfolio.observeForever(portfolioObserver)
        }
    }

    @After
    fun after() {
        viewModel.apply {
            portfolio.removeObserver(portfolioObserver)
        }
    }

    @Test
    fun testHappyPath() = runBlocking {
        // Arrange
        val endpoint = PortfolioEndpoint.Good
        val stock = Stock(
            currency = "",
            current_price_cents = null,
            current_price_timestamp = 0,
            name = "",
            quantity = null,
            ticker = "",
            ratio = 0.0f
        )

        val portfolio = Portfolio(listOf( stock, stock, stock))
        val resultWrapper = ResultWrapper.Success(portfolio)

        whenever(repository.loadPortfolio(endpoint)).thenReturn(resultWrapper)

        // Act
        viewModel.loadStocksInternal(endpoint)

        // Assert
        verify(portfolioObserver).onChanged(resultWrapper)
    }
}