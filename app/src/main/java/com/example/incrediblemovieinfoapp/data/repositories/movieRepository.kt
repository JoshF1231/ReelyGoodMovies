package com.example.incrediblemovieinfoapp.data.repositories

import android.app.Application
import com.example.incrediblemovieinfoapp.data.local_db.MovieDao
import com.example.incrediblemovieinfoapp.data.local_db.MovieDataBase
import com.example.incrediblemovieinfoapp.data.models.Movie

class movieRepository(application: Application) {

    private var movieDao: MovieDao?

    init{
        val db = MovieDataBase.getDataBase(application.applicationContext)
        movieDao = db?.movieDao()
    }

    fun getMovies() = movieDao?.getMovies()

    fun addMovie(movie: Movie) {
        movieDao?.addMovie(movie)
    }

    fun updateMovie(movie: Movie){
        movieDao?.updateMovie(movie)
    }

    fun deleteMovie(movie: Movie) {
        movieDao?.deleteMovie(movie)
    }

    fun deleteAllMovies(){
        movieDao?.deleteAllMovies()
    }

}