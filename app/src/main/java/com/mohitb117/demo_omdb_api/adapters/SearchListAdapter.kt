package com.mohitb117.demo_omdb_api.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.mohitb117.demo_omdb_api.databinding.ItemMovieListPreviewBinding
import com.mohitb117.demo_omdb_api.datamodels.SearchResult
import com.bumptech.glide.Glide
import com.mohitb117.demo_omdb_api.repositories.MovieRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SearchListAdapter
constructor(
    diffCallback: VideosDiffCallback,
    private val repository: MovieRepository
) : ListAdapter<SearchResult, SearchListAdapter.ViewHolder>(diffCallback) {

    /**
     * FIXME: This is not an ideal implementation as i would use ViewModel Events but strapped for time.
     */
    var callbacks: Callbacks? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemMovieListPreviewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position), position)
    }

    inner class ViewHolder(private val binding: ItemMovieListPreviewBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(searchResult: SearchResult, position: Int) {
            binding.root.setOnClickListener {
                callbacks?.gotoDetails(searchResult.imdbID, position)
            }

            CoroutineScope(Dispatchers.Main).launch {
                binding.apply {
                    starredImageview.isVisible = withContext(Dispatchers.IO) { repository.isFavourite(searchResult.imdbID) }
                    name.text = searchResult.title
                    type.text = searchResult.type

                    Glide.with(binding.root)
                        .load(searchResult.poster)
                        .into(binding.posterPreview)
                    }
            }
        }
    }
}