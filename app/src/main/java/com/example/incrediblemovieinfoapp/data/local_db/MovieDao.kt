package com.example.incrediblemovieinfoapp.data.local_db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.incrediblemovieinfoapp.data.models.Movie

@Dao
interface MovieDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addMovie(movie: Movie)

    @Delete
    fun deleteMovie(vararg movies: Movie)

    @Update
    fun updateMovie(movie: Movie)

    @Query("SELECT * FROM movies")
    fun getMovies(): LiveData<List<Movie>>

    @Query("SELECT * FROM movies WHERE id LIKE :id")
    fun getMovie(id: Int) : Movie

    @Query("DELETE FROM movies")
    fun deleteAllMovies()
}




