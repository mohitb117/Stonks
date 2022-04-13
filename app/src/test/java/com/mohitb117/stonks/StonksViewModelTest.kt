package com.mohitb117.stonks

import com.mohitb117.stonks.repositories.StonksRepository
import com.mohitb117.stonks.ui.stocks.StonksViewModel
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class StonksViewModelTest {

    private lateinit var viewModel: StonksViewModel

    @Mock
    private lateinit var repository: StonksRepository

    @Before
    fun before() {
        viewModel = StonksViewModel(repository)
    }

    @Test
    fun test() {
        // Arrange

        // Act

        // Assert
    }
}