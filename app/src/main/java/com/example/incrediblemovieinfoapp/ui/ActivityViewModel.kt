package com.example.incrediblemovieinfoapp.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.incrediblemovieinfoapp.data.model.Movie

class ActivityViewModel : ViewModel() {
    private var _movieList :MutableLiveData<List<Movie>> = MutableLiveData(emptyList())
    val movieList : LiveData<List<Movie>> get() = _movieList
    private var selectedItemIndex = -1


    private val _chosenMovie = MutableLiveData<Movie>()
    val chosenMovie: LiveData<Movie> get() = _chosenMovie

    fun setMovie(movie: Movie){
        _chosenMovie.value = movie
    }


    fun addMovie(movie : Movie){
        _movieList.value = _movieList.value?.plus(movie)
    }
    fun removeMovie(index: Int) {
        _movieList.value = _movieList.value?.toMutableList()?.apply {
            removeAt(index)
        }
    }
    fun removeMovie(movie : Movie){
        _movieList.value = _movieList.value?.minus(movie)
    }

    fun getMovieAt(index: Int) : Movie?{
        return movieList.value?.get(index)
    }

    fun getSelectedMovieIndex() : Int {
        return selectedItemIndex
    }
    fun setSelectedMovieIndex(index : Int) {
        selectedItemIndex = index
    }


}