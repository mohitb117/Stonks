package com.mohitb117.demo_omdb_api.datamodels

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

/**
 * View specific data object to display video previews
 */

@Entity(tableName = PERSISTED_MOVIE_TABLE_NAME)
@Parcelize
data class SearchResult(
    @PrimaryKey val imdbID: String,
    @SerializedName("Title") val title: String,
    @SerializedName("Type") val type: String,
    @SerializedName("Poster") val poster: String
) : Parcelable

@Parcelize
data class SearchResultsBody(
    val Search: List<SearchResult>? = null,
    val totalResults: String,
    val Response: String
) : Parcelable

const val PERSISTED_MOVIE_TABLE_NAME = "persisted_movie_table"

/**
 * movie name, director, year, brief plot summary, and poster
 */
@Parcelize
data class DetailsResultsBody(
    val Actors: String,
    val Awards: String,
    val BoxOffice: String,
    val Country: String,
    val DVD: String,
    val Director: String,
    val Genre: String,
    val Language: String,
    val Metascore: String,
    val Plot: String,
    val Poster: String,
    val Production: String,
    val Rated: String,
    val Ratings: List<Rating>,
    val Released: String,
    val Response: String,
    val Runtime: String,
    val Title: String,
    val Type: String,
    val Website: String,
    val Writer: String,
    val Year: String,
    val imdbID: String,
    val imdbRating: String,
    val imdbVotes: String
):Parcelable {
    fun toMovieSearchResult() = SearchResult(imdbID, Title, Type, Poster)
}

@Parcelize
data class Rating(
    val Source: String,
    val Value: String
):Parcelable
