package com.example.incrediblemovieinfoapp

import android.content.ClipData.Item
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ActivityViewModel : ViewModel() {
    private var mutableMovieList :MutableLiveData<List<Movie>> = MutableLiveData(emptyList())
    val movieList : LiveData<List<Movie>> get() = mutableMovieList

    fun addMovie(movie : Movie){
        val updatedList = mutableMovieList.value?.toMutableList()?.apply {
            add(movie)
        } ?: mutableListOf(movie)
        mutableMovieList.value = updatedList
    }
    fun removeMovie(movie : Movie){
        mutableMovieList.value?.minus(movie)
    }

    companion object Genres{

    }
}