package com.mohitb117.demo_omdb_api.adapters

import androidx.recyclerview.widget.DiffUtil
import com.mohitb117.demo_omdb_api.datamodels.SearchResult

class VideosDiffCallback : DiffUtil.ItemCallback<SearchResult>() {

    override fun areItemsTheSame(oldItem: SearchResult, newItem: SearchResult): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(
        oldItem: SearchResult,
        newItem: SearchResult
    ): Boolean {
        return oldItem.imdbID == newItem.imdbID
    }
}