package com.example.incrediblemovieinfoapp.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.incrediblemovieinfoapp.data.model.Movie

class ActivityViewModel : ViewModel() {
    private var mutableMovieList :MutableLiveData<List<Movie>> = MutableLiveData(emptyList())
    val movieList : LiveData<List<Movie>> get() = mutableMovieList
    private var selectedItemIndex = -1

    fun addMovie(movie : Movie){
        mutableMovieList.value = mutableMovieList.value?.plus(movie)
    }
    fun removeMovie(index: Int) {
        mutableMovieList.value = mutableMovieList.value?.toMutableList()?.apply {
            removeAt(index)
        }
    }
    fun removeMovie(movie : Movie){
        mutableMovieList.value = mutableMovieList.value?.minus(movie)
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

    companion object Genres{

    }
}