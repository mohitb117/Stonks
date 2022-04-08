package com.mohitb117.stonks.ui.favourites

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.mohitb117.stonks.R
import com.mohitb117.stonks.activities.LaunchingActivity
import com.mohitb117.stonks.adapters.Callbacks
import com.mohitb117.stonks.databinding.FragmentFavouritesBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

/**
 * UI to preview the persisted data for a user.
 */
@AndroidEntryPoint
class FavouritesFragment : Fragment() {

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

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Listen for State Update for items being marked as "fav" and selectively update the item.
        favouritesViewModel.getFavItems()
            .observe(viewLifecycleOwner) { favItems -> }
    }
}