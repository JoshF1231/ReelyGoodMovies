package com.example.reelygoodmovies.data.repositories

import android.app.Application
import com.example.reelygoodmovies.data.local_db.MovieDao
import com.example.reelygoodmovies.data.local_db.MovieDataBase
import com.example.reelygoodmovies.data.models.Movie
import javax.inject.Singleton

@Singleton
class MovieRepository(application: Application) {

    private var movieDao: MovieDao?

    init {
        val db = MovieDataBase.getDataBase(application.applicationContext)
        movieDao = db.movieDao()
    }

    fun getMovies() = movieDao?.getMovies()

    fun getMovie(id: Int) = movieDao?.getMovie(id)

    fun getFavoriteMovies() = movieDao?.getFavoriteMovies()

    suspend fun addMovie(movie: Movie) {
        movieDao?.addMovie(movie)
    }

    suspend fun updateMovie(movie: Movie) {
        movieDao?.updateMovie(movie)
    }

    suspend fun deleteMovie(movie: Movie) {
        movieDao?.deleteMovie(movie)
    }

    suspend fun deleteAllMovies() {
        movieDao?.deleteAllMovies()
    }




}