package com.example.reelygoodmovies.ui.detail_movie

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import com.example.reelygoodmovies.data.models.Movie
import com.example.reelygoodmovies.utils.MovieRepositoryNew
import com.example.reelygoodmovies.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DetailedMovieViewModel @Inject constructor(
    private val movieRepositoryNew: MovieRepositoryNew
) : ViewModel() {
    private val _id = MutableLiveData<Int>()
    private val _movie = _id.switchMap {
        movieRepositoryNew.getMovie(it)
    }
    val movie: LiveData<Resource<Movie>> = _movie
    fun setId(id: Int) {
        _id.value = id
    }
}