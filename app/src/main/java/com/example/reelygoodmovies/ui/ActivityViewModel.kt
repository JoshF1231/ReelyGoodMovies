package com.example.reelygoodmovies.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.reelygoodmovies.data.models.Movie
import com.example.reelygoodmovies.data.repositories.MovieRepository
import kotlinx.coroutines.launch


class ActivityViewModel(
    application: Application,
) : AndroidViewModel(application) {


    private val repository = MovieRepository(application)
    val movieList: LiveData<List<Movie>>? = repository.getMovies()

    val favoriteMovies: LiveData<List<Movie>>? = repository.getFavoriteMovies()

    private val _chosenMovie = MutableLiveData<Movie>()
    val chosenMovie: LiveData<Movie> get() = _chosenMovie

    private val _selectedYear = MutableLiveData<Int>()
    val selectedYear: LiveData<Int> get() = _selectedYear

    private val _selectedRuntimeHours = MutableLiveData<Int>()
    val selectedRuntimeHours: LiveData<Int> get() = _selectedRuntimeHours

    private val _selectedRuntimeMinutes = MutableLiveData<Int>()
    val selectedRuntimeMinutes: LiveData<Int> get() = _selectedRuntimeMinutes

    private val _selectedImageURI = MutableLiveData<String>()
    val selectedImageURI: LiveData<String> get() = _selectedImageURI

    private val _isEditMode = MutableLiveData(false)
    val isEditMode: LiveData<Boolean> get() = _isEditMode

    private val _favorite = MutableLiveData<Boolean>()
    val favorite: LiveData<Boolean> get() = _favorite


    private val _filteredMovies = MutableLiveData<List<Movie>>()
    val filteredMovies: LiveData<List<Movie>> get() = _filteredMovies

    fun setFilteredMovies(movies: List<Movie>) {
        _filteredMovies.value = movies

    }

    fun setEditMode(isEdit: Boolean) {
        _isEditMode.value = isEdit
    }

    fun setSelectedImageURI(uri: String?) {
        _selectedImageURI.value = uri ?: ""
    }

    fun setSelectedRuntimeHours(hours: Int) {
        _selectedRuntimeHours.value = hours
    }

    fun setSelectedRuntimeMinutes(minutes: Int) {
        _selectedRuntimeMinutes.value = minutes
    }

    fun setSelectedYear(year: Int) {
        _selectedYear.value = year
    }

    fun setMovie(movie: Movie) {
        _chosenMovie.value = movie
        //_favorite.value = movie.favorite
    }

    fun addMovie(movie: Movie) {
        viewModelScope.launch { repository.addMovie(movie) }
    }

    fun deleteMovie(movie: Movie) {
        viewModelScope.launch { repository.deleteMovie(movie) }
    }

    fun deleteAllMovies() {
        viewModelScope.launch { repository.deleteAllMovies() }
    }

    fun updateMovie(movie: Movie) {
        viewModelScope.launch { repository.updateMovie(movie) }
    }

    fun setFavorite(bool: Boolean) {
        _favorite.value = bool
    }

    fun clearAllData() {
        setSelectedYear(0)
        setSelectedRuntimeHours(0)
        setSelectedRuntimeMinutes(0)
        setSelectedImageURI(null)
        setFavorite(false)
    }
}