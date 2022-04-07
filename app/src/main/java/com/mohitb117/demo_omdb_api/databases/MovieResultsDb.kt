package com.mohitb117.demo_omdb_api.databases

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.mohitb117.demo_omdb_api.DB_NAME
import com.mohitb117.demo_omdb_api.dao.FavouritesDao
import com.mohitb117.demo_omdb_api.datamodels.SearchResult

// Annotates class to be a Room Database with a table (entity) of class
@Database(entities = [SearchResult::class], version = 1, exportSchema = true)
public abstract class MovieResultsDb : RoomDatabase() {

    abstract fun favouritesDao(): FavouritesDao

    companion object {
        // Singleton prevents multiple instances of database
        // opening at the same time.
        @Volatile
        private var INSTANCE: MovieResultsDb? = null

        fun getDatabase(context: Context): MovieResultsDb {
            // if the INSTANCE is not null, then return it,
            // if it is, then create the database

            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    MovieResultsDb::class.java,
                    DB_NAME
                ).build()

                INSTANCE = instance
                // return instance
                instance
            }
        }
    }
}