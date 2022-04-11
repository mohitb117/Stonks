package com.mohitb117.stonks.ui.details

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.snackbar.Snackbar
import com.mohitb117.stonks.SEARCH_KEY
import com.mohitb117.stonks.databinding.FragmentDetailsBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@AndroidEntryPoint
class BottomSheetDetailsFragment : BottomSheetDialogFragment() {

    private lateinit var viewBinding: FragmentDetailsBinding
    private val detailsViewModel: BottomSheetDetailsViewModel by viewModels()
    private var snackbar: Snackbar? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewBinding = FragmentDetailsBinding.inflate(inflater, container, false)

        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT));

        return viewBinding.root
    }

//    private fun handleFavouriteToggle() {
//        viewBinding.favouriteToggle.setOnCheckedChangeListener { _, isChecked ->
//            when (isChecked) {
//                true -> detailsViewModel.insert(cached?.toMovieSearchResult()!!)
//                false -> detailsViewModel.delete(imdbKey())
//            }
//        }
//    }

//    private fun imdbKey() = arguments?.getString(SEARCH_KEY)!!
//
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//
//        handleFavouriteToggle()
//
//        detailsViewModel.loadDetailsResult(imdbKey())
//
//        detailsViewModel.viewState.observe(viewLifecycleOwner) { detailsViewState: DetailsViewState? ->
//            handleDetailsViewState(detailsViewState)
//        }
//    }
//
//    private fun handleDetailsViewState(detailsViewState: DetailsViewState?) {
//        val result = detailsViewState?.searchResult
//        this.cached = result
//
//        if (result != null) {
//            viewBinding.apply {
//                this.name.text = result.Title
//                this.plotSummary.text = result.Plot
//                this.year.text = result.Year
//                this.director.text = result.Director
//
//                lifecycleScope.launch {
//                    favouriteToggle.isChecked = withContext(Dispatchers.IO) { detailsViewModel.isFavourite(result.imdbID) }
//                }
//
//                Glide.with(this@BottomSheetDetailsFragment)
//                    .applyDefaultRequestOptions(requestOptions)
//                    .load(result.Poster)
//                    .into(poster)
//            }
//        }
//    }

    override fun onDestroy() {
        super.onDestroy()
        snackbar?.dismiss()
    }
}