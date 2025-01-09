package com.example.incrediblemovieinfoapp.data.local_db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.incrediblemovieinfoapp.data.models.IntListConverter
import com.example.incrediblemovieinfoapp.data.models.Movie

@TypeConverters(IntListConverter::class)
@Database(entities = [Movie::class], version = 3, exportSchema = false)
abstract class MovieDataBase : RoomDatabase() {

    abstract fun movieDao(): MovieDao

    companion object {

        @Volatile
        private var instance: MovieDataBase? = null

        fun getDataBase(context: Context): MovieDataBase {
            return instance ?: synchronized(this) {
                instance ?: synchronized(this) {
                    Room.databaseBuilder(
                        context.applicationContext,
                        MovieDataBase::class.java,
                        "movies_db"
                    )
                        .fallbackToDestructiveMigration()
                        .build()
                        .also { instance = it }
                }

            }
        }
    }
}

