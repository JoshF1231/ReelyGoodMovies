package com.example.reelygoodmovies.data.local_db

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.reelygoodmovies.data.models.Movie
import javax.inject.Inject

@Dao
interface MovieDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addMovie(movie: Movie)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addMovies(movies : List <Movie>)

    @Delete
    suspend fun deleteMovie(vararg movies: Movie)

    @Update
    suspend fun updateMovie(movie: Movie)

    @Query("SELECT * FROM movies")
    fun getMovies(): LiveData<List<Movie>>

    @Query("SELECT * FROM movies WHERE id = :id")
    fun getMovie(id: Int): LiveData<Movie>

    @Query("DELETE FROM movies")
    suspend fun deleteAllMovies()

    @Query("SELECT * FROM movies WHERE favorite = 1")
    fun getFavoriteMovies(): LiveData<List<Movie>>


}
