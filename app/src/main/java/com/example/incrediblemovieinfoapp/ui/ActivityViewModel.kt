package com.example.incrediblemovieinfoapp.ui

import android.app.Application
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.map
import com.bumptech.glide.load.Transformation
import com.example.incrediblemovieinfoapp.data.models.Movie
import com.example.incrediblemovieinfoapp.data.repositories.movieRepository
import kotlin.time.Duration.Companion.minutes


class ActivityViewModel(
    application: Application,
    private val savedStateHandle: SavedStateHandle
) : AndroidViewModel(application) {


    private val repository = movieRepository(application)
    val movieList: LiveData<List<Movie>>? = repository.getMovies()


    private val _chosenMovie = MutableLiveData<Movie>()
    val chosenMovie: LiveData<Movie> get() = _chosenMovie

    private val _selectedYear = MutableLiveData<Int>()
    val selectedYear : LiveData<Int> get() = _selectedYear

    private val _selectedRuntimeHours = MutableLiveData<Int>()
    val selectedRuntimeHours : LiveData<Int> get() = _selectedRuntimeHours

    private val _selectedRuntimeMinutes = MutableLiveData<Int>()
    val selectedRuntimeMinutes : LiveData<Int> = _selectedRuntimeMinutes.map { minutes ->
        minutes ?: 0
    }

    private val _selectedImageURI = MutableLiveData<String>()
    val selectedImageURI : LiveData<String> get() = _selectedImageURI

    fun setSelectedImageURI(uri : String?){
        if (uri!= null){
            _selectedImageURI.value = uri!!
        }
    }

    fun setSelectedRuntimeHours(hours : Int){
        _selectedRuntimeHours.value = hours
    }

    fun setSelectedRuntimeMinutes(minutes : Int){
        _selectedRuntimeMinutes.value = minutes
    }

    fun setSelectedYear (year:Int){
        _selectedYear.value = year
    }


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