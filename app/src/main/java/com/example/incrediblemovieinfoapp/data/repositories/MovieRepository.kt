package com.example.incrediblemovieinfoapp.data.repositories

import android.app.Application
import com.example.incrediblemovieinfoapp.data.local_db.MovieDao
import com.example.incrediblemovieinfoapp.data.local_db.MovieDataBase
import com.example.incrediblemovieinfoapp.data.models.Movie

class MovieRepository(application: Application) {

    private var movieDao: MovieDao?

    init{
        val db = MovieDataBase.getDataBase(application.applicationContext)
        movieDao = db.movieDao()
    }

    fun getMovies() = movieDao?.getMovies()

    suspend fun addMovie(movie: Movie) {
        movieDao?.addMovie(movie)
    }

    suspend fun updateMovie(movie: Movie){
        movieDao?.updateMovie(movie)
    }

    suspend fun deleteMovie(movie: Movie) {
        movieDao?.deleteMovie(movie)
    }

    suspend fun deleteAllMovies(){
        movieDao?.deleteAllMovies()
    }

}