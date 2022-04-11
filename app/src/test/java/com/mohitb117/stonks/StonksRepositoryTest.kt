package com.mohitb117.stonks

import com.mohitb117.stonks.dao.CachedPortfolioDao
import com.mohitb117.stonks.datamodels.Portfolio
import com.mohitb117.stonks.datamodels.Stock
import com.mohitb117.stonks.endpoints.DetailsApi
import com.mohitb117.stonks.endpoints.ListApi
import com.mohitb117.stonks.repositories.StonksRepository
import com.slack.eithernet.ApiResult
import kotlinx.coroutines.runBlocking
import org.junit.Test

import org.junit.Assert.*
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.stubbing.OngoingStubbing

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
private const val FAKE_STRING = "HELLO WORLD"

@Suppress("NOTHING_TO_INLINE")
inline fun <T> whenever(methodCall: T): OngoingStubbing<T> {
    return `when`(methodCall)!!
}

@RunWith(MockitoJUnitRunner::class)
class StonksRepositoryTest {

    @Mock
    private lateinit var mockListApi: ListApi

    @Mock
    private lateinit var mockDetailsApi: DetailsApi

    @Mock
    private lateinit var dao: CachedPortfolioDao

    @Test
    fun dummyApiWithSuccess() = runBlocking {
        // Arrange
        val successfulResult = ApiResult.success(Portfolio(emptyList()))
        whenever(mockListApi.loadPortfolioGood()).thenReturn(successfulResult)

        val repository = StonksRepository(mockListApi,mockDetailsApi, dao)

        // Act.
        val result = repository.loadPortfolio()

        // Assert.
        assertTrue(result is ApiResult.Success)
        assertEquals(successfulResult.value, (result as ApiResult.Success).value)
    }

    @Test
    fun dummyApiWithError() = runBlocking {
        // Arrange
        val error = "error"
        val errorResponse = ApiResult.httpFailure(404, error)
        whenever(mockListApi.loadPortfolioGood()).thenReturn(errorResponse)

        val repository = StonksRepository(mockListApi,mockDetailsApi, dao)

        // Act.
        val result = repository.loadPortfolio()

        // Assert.
        assertTrue(result is ApiResult.Failure)
        assertEquals(404, (result as ApiResult.Failure.HttpFailure).code)
        assertEquals(error, result.error)
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

        val repository = StonksRepository(mockListApi,mockDetailsApi, dao)

        // Act.
        val result = repository.contains("QQQ")

        // Assert.
        assertTrue(result)
    }

    @Test
    fun repositoryReturnsTrueWhenDbHasNoValue() = runBlocking {
        // Arrange
        whenever(dao.getForTicker("AAAA")).thenReturn(emptyList())

        val repository = StonksRepository(mockListApi,mockDetailsApi, dao)

        // Act.
        val result = repository.contains("AAAA")

        // Assert.
        assertFalse(result)
    }
}