package com.example.incrediblemovieinfoapp.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.incrediblemovieinfoapp.data.models.Movie
import com.example.incrediblemovieinfoapp.data.repositories.movieRepository


class ActivityViewModel(application: Application) : AndroidViewModel(application) {


    private val repository = movieRepository(application)
    val movieList: LiveData<List<Movie>>? = repository.getMovies()



    private val _chosenMovie = MutableLiveData<Movie>()
    val chosenMovie: LiveData<Movie> get() = _chosenMovie

    fun setMovie(movie: Movie){
        _chosenMovie.value = movie
    }


    fun addMovie(movie: Movie){
        repository.addMovie(movie)
    }

    fun deleteMovie(movie: Movie){
        repository.deleteMovie(movie)
    }

    fun deleteAllMovies(){
        repository.deleteAllMovies()
    }


}