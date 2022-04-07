package com.mohitb117.demo_omdb_api.ui.search

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mohitb117.demo_omdb_api.datamodels.SearchResultsBody
import com.mohitb117.demo_omdb_api.repositories.MovieRepository
import com.slack.eithernet.ApiResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

data class SearchViewState(
    val searchTerm: String = "dogs",
    val searchResult: SearchResultsBody? = null,
    val errorReceived: String? = null
)

/**
 * ViewModel responsible for fetching / loading results from OMDB.
 */
@HiltViewModel
class SearchViewModel
@Inject constructor(
    private val repository: MovieRepository
) : ViewModel() {

    private val _viewState: MutableLiveData<SearchViewState?> = MutableLiveData(null)
    val viewState: LiveData<SearchViewState?> = _viewState

    fun loadSearchResult(searchTerm: String) {
        val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
            Log.e(SearchViewModel::class.java.simpleName, "Error Encountered: ${throwable.localizedMessage}")
            _viewState.postValue(SearchViewState(searchTerm, null, throwable.localizedMessage))
        }

        viewModelScope.launch(Dispatchers.IO + exceptionHandler) {
            when(val result = repository.loadResults(searchTerm)) {
                is ApiResult.Success -> _viewState.postValue(SearchViewState(searchTerm, result.value))

                is ApiResult.Failure -> when(result) {
                    is ApiResult.Failure.ApiFailure -> _viewState.postValue(SearchViewState(searchTerm, null, result.error.toString()))
                    is ApiResult.Failure.HttpFailure -> _viewState.postValue(SearchViewState(searchTerm, null, result.error.toString()))
                    is ApiResult.Failure.NetworkFailure -> _viewState.postValue(SearchViewState(searchTerm, null, result.error.toString()))
                    is ApiResult.Failure.UnknownFailure -> _viewState.postValue(SearchViewState(searchTerm, null, result.error.toString()))
                    else -> _viewState.postValue(SearchViewState(searchTerm, null, "Not sure what is going on!!! :sob: "))
                }
            }
        }
    }

    fun getFavItems() = repository.getFavItems()
}