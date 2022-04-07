package com.mohitb117.demo_omdb_api.ui.favourites

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.mohitb117.demo_omdb_api.R
import com.mohitb117.demo_omdb_api.activities.LaunchingActivity
import com.mohitb117.demo_omdb_api.adapters.Callbacks
import com.mohitb117.demo_omdb_api.adapters.SearchListAdapter
import com.mohitb117.demo_omdb_api.databinding.FragmentFavouritesBinding
import com.mohitb117.demo_omdb_api.datamodels.SearchResult
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

/**
 * UI to preview the persisted data for a user.
 */
@AndroidEntryPoint
class FavouritesFragment : Fragment() {

    @Inject
    internal lateinit var adapter: SearchListAdapter

    private val favouritesViewModel: FavouritesViewModel by viewModels()

    private lateinit var binding: FragmentFavouritesBinding

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

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFavouritesBinding.inflate(inflater, container, false)

        binding.resultsRecyclerView.layoutManager = GridLayoutManager(
            binding.root.context,
            resources.getInteger(R.integer.column_count)
        )

        binding.resultsRecyclerView.adapter = adapter
        adapter.callbacks = searchCallbacks

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Listen for State Update for items being marked as "fav" and selectively update the item.
        favouritesViewModel.getFavItems()
            .observe(viewLifecycleOwner) { favItems: List<SearchResult>? -> handleFavouriteResults(favItems) }
    }

    @SuppressLint("SetTextI18n")
    private fun handleFavouriteResults(favItems: List<SearchResult>?) {
        val hasResults = favItems?.isNotEmpty() ?: false

        binding.apply {
            emptyView.isInvisible = hasResults
            resultsRecyclerView.isVisible = hasResults
            resultsSummary.text = "Fav Summary returned: ${favItems?.size}"
        }

        if (hasResults) {
            adapter.submitList(favItems)
        }
        adapter.submitList(favItems)
    }
}