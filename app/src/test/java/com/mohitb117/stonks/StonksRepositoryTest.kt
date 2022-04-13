package com.mohitb117.stonks

import com.mohitb117.stonks.common.ResultWrapper
import com.mohitb117.stonks.dao.CachedPortfolioDao
import com.mohitb117.stonks.datamodels.Portfolio
import com.mohitb117.stonks.datamodels.Stock
import com.mohitb117.stonks.endpoints.DetailsApi
import com.mohitb117.stonks.endpoints.ListApi
import com.mohitb117.stonks.repositories.PortfolioEndpoint
import com.mohitb117.stonks.repositories.StonksRepository
import com.mohitb117.stonks.ui.stocks.StonksViewModel
import com.slack.eithernet.ApiResult
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.Test

import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.stubbing.OngoingStubbing

@Suppress("NOTHING_TO_INLINE")
inline fun <T> whenever(methodCall: T): OngoingStubbing<T> {
    return `when`(methodCall)!!
}

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(MockitoJUnitRunner::class)
class StonksRepositoryTest {
    @Mock
    private lateinit var mockListApi: ListApi

    @Mock
    private lateinit var mockDetailsApi: DetailsApi

    @Mock
    private lateinit var dao: CachedPortfolioDao

    private lateinit var repository: StonksRepository
    
    @get:Rule
    val coroutineTestRule = CoroutineTestRule()
    
    @Before
    fun before() {
        repository = StonksRepository(mockListApi, mockDetailsApi, dao)
    }

    @Test
    fun dummyApiWithSuccess() = runBlocking {
        // Arrange
        val successfulResult = ApiResult.success(Portfolio(emptyList()))
        whenever(mockListApi.loadPortfolioGood()).thenReturn(successfulResult)

        // Act.
        val result = repository.loadPortfolio(PortfolioEndpoint.Good)

        // Assert.
        assertTrue(result is ResultWrapper.Success)
        assertEquals(successfulResult.value, (result as ResultWrapper.Success).data)
        assertFalse(result.isCachedData)
    }

    @Test
    fun dummyApiWithError() = runBlocking {
        // Arrange
        val error = "error"
        val errorResponse = ApiResult.httpFailure(404, error)
        whenever(mockListApi.loadPortfolioGood()).thenReturn(errorResponse)

        // Act.
        val result = repository.loadPortfolio(PortfolioEndpoint.Good)

        // Assert.
        assertTrue(result is ResultWrapper.Error)
        assertEquals(error, (result as ResultWrapper.Error).error)
    }

    @Test
    fun repositoryReturnsTrueWhenDbHasValidValue() = runBlocking {
        // Arrange
        val stock = Stock(
            currency = "",
            current_price_cents = null,
            current_price_timestamp = 0,
            name = "QQQ",
            quantity = null,
            ticker = "QQQ",
            ratio = 0.0f
        )

        whenever(dao.getForTicker("QQQ")).thenReturn(listOf(stock))

        // Act.
        val result = repository.contains("QQQ")

        // Assert.
        assertTrue(result)
    }

    @Test
    fun repositoryReturnsTrueWhenDbHasNoValue() = runBlocking {
        // Arrange
        whenever(dao.getForTicker("AAAA")).thenReturn(emptyList())

        // Act.
        val result = repository.contains("AAAA")

        // Assert.
        assertFalse(result)
    }
}