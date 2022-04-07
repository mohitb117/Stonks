package com.mohitb117.demo_omdb_api.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.mohitb117.demo_omdb_api.datamodels.SearchResult
import com.mohitb117.demo_omdb_api.datamodels.PERSISTED_MOVIE_TABLE_NAME

@Dao
interface FavouritesDao {

    @Query(
        """
       Select * from $PERSISTED_MOVIE_TABLE_NAME
       """
    )
    fun getAllFavItems(): LiveData<List<SearchResult>>

    @Query(
        """
       Select * from $PERSISTED_MOVIE_TABLE_NAME
       where imdbID == :imdbId
       """
    )
    suspend fun getForId(imdbId: String): List<SearchResult>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(entity: SearchResult)

    @Query(
        """
        DELETE FROM $PERSISTED_MOVIE_TABLE_NAME 
        where imdbID == :imdbId
        """
    )
    suspend fun delete(imdbId: String)
}