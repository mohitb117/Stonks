package com.mohitb117.stonks.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.mohitb117.stonks.datamodels.Stock
import com.mohitb117.stonks.datamodels.TABLE_NAME

@Dao
interface CachedPortfolioDao {

    @Query(
        """
       Select * from $TABLE_NAME
       """
    )
    fun getStocks(): LiveData<List<Stock>>

    @Query(
        """
       Select * from $TABLE_NAME
       where ticker == :ticker
       """
    )
    suspend fun getForTicker(ticker: String): List<Stock>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(entity: Stock)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(entity: List<Stock>)

    @Query(
        """
        DELETE FROM $TABLE_NAME 
        where ticker == :ticker
        """
    )
    suspend fun delete(ticker: String)

    @Query(
        """
        DELETE FROM $TABLE_NAME 
        """
    )
    suspend fun deleteAll()
}