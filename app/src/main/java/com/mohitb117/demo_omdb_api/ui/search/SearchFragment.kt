package com.mohitb117.demo_omdb_api.ui.search

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.*
import android.view.inputmethod.EditorInfo
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.mohitb117.demo_omdb_api.R
import com.mohitb117.demo_omdb_api.adapters.SearchListAdapter
import com.mohitb117.demo_omdb_api.databinding.FragmentSearchBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

import android.widget.TextView
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import com.mohitb117.demo_omdb_api.activities.LaunchingActivity
import com.mohitb117.demo_omdb_api.adapters.Callbacks
import com.mohitb117.demo_omdb_api.datamodels.SearchResult

/**
 * UI to search the OMDB API.
 */
@AndroidEntryPoint
class SearchFragment : Fragment() {

    private lateinit var viewBinding: FragmentSearchBinding

    @Inject
    internal lateinit var adapter: SearchListAdapter

    private val searchViewModel: SearchViewModel by viewModels()

    /**
     * HACK: This should ideally be propagated as separate events / Kotlin Coroutine Flow + DB.
     */
    private var lastSelectedPosition: Int? = null

    private val searchCallbacks = object : Callbacks {
        override fun gotoDetails(imdbId: String, position: Int) {
            (activity as LaunchingActivity).gotoDetails(imdbId)
            lastSelectedPosition = position
        }
    }

    private val nextListener = object : TextView.OnEditorActionListener {
        override fun onEditorAction(textView: TextView?, actionId: Int, event: KeyEvent?): Boolean {
            if (actionId == EditorInfo.IME_ACTION_NEXT) {
                processSearch()
                return true
            }
            return false
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewBinding = FragmentSearchBinding.inflate(inflater, container, false)

        viewBinding.resultsRecyclerView.layoutManager = GridLayoutManager(
            viewBinding.root.context,
            resources.getInteger(R.integer.column_count)
        )
        viewBinding.resultsRecyclerView.adapter = adapter
        adapter.callbacks = searchCallbacks

        return viewBinding.root
    }

    private fun processSearch() {
        val searchKey = viewBinding.searchTermEdittext.text.toString()
        searchViewModel.loadSearchResult(searchKey)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewBinding.apply {
            submitButton.setOnClickListener { processSearch() }
            searchTermEdittext.setOnEditorActionListener(nextListener)
        }

        // Listen for Search Result Update.
        searchViewModel.viewState
            .observe(viewLifecycleOwner) { viewState -> handleSearchViewState(viewState) }

        // Listen for State Update for items being marked as "fav" and selectively update the item.
        searchViewModel.getFavItems()
            .observe(viewLifecycleOwner) { favItems: List<SearchResult>? -> updateAdapter(favItems) }
    }

    private fun updateAdapter(favItems: List<SearchResult>?) {
        if (favItems?.isNotEmpty() == true) {

            val position = lastSelectedPosition
            if (position != null) {
                adapter.notifyItemChanged(position)
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun handleSearchViewState(viewState: SearchViewState?) {
        viewState?.errorReceived?.let {
            viewBinding.resultsSummary.apply {
                text = " Error Received: $it"
                setTextColor(resources.getColor(android.R.color.holo_red_dark, context.theme))
            }
            Log.e(TAG, "Error Received $it")
        }

        viewState?.searchResult?.let {
            val hasResults = it.Search?.isNotEmpty() ?: false

            viewBinding.apply {
                emptyView.isInvisible = hasResults
                resultsRecyclerView.isVisible = hasResults

                resultsSummary.apply {
                    text = """
                        Result summary for ${viewState.searchTerm} returned: ${it.totalResults}  
                        but page #1 loaded ${it.Search?.size} results
                    """.trimIndent()

                    setTextColor(resources.getColor(android.R.color.holo_green_light, context.theme))
                }
            }

            if (hasResults) {
                adapter.submitList(it.Search)
            }
        }
    }

    private companion object {
        private val TAG = SearchFragment::class.java.simpleName
    }
}