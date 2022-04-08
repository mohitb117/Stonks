package com.mohitb117.stonks.databases

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.mohitb117.stonks.DB_NAME
import com.mohitb117.stonks.dao.CachedPortfolioDao
import com.mohitb117.stonks.datamodels.Stock

// Annotates class to be a Room Database with a table (entity) of class
@Database(
    entities = [
        Stock::class
    ],
    version = 1,
    exportSchema = true
)
abstract class PortfolioDb : RoomDatabase() {

    abstract fun favouritesDao(): CachedPortfolioDao

    companion object {
        @Volatile
        private var INSTANCE: PortfolioDb? = null

        fun getDatabase(context: Context): PortfolioDb {
            // if the INSTANCE is not null, then return it,
            // if it is, then create the database

            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(context.applicationContext, PortfolioDb::class.java, DB_NAME)
                    .fallbackToDestructiveMigration()
                    .build()

                INSTANCE = instance
                instance
            }
        }
    }
}