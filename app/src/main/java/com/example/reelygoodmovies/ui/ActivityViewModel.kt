package com.example.reelygoodmovies.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.reelygoodmovies.R
import com.example.reelygoodmovies.data.models.Movie
import com.example.reelygoodmovies.data.repositories.MovieRepository
import com.example.reelygoodmovies.utils.MovieRepositoryNew
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ActivityViewModel @Inject constructor(
    application: Application, moviesRepository : MovieRepositoryNew
) : AndroidViewModel(application) {

    val movies = moviesRepository.getMovies()

    private val repository = MovieRepository(application)
    val movieList: LiveData<List<Movie>>? = repository.getMovies()

    val favoriteMovies: LiveData<List<Movie>>? = repository.getFavoriteMovies()

    private val _chosenMovie = MutableLiveData<Movie>()
    val chosenMovie: LiveData<Movie> get() = _chosenMovie

//    private val _selectedYear = MutableLiveData<Int>()
//    val selectedYear: LiveData<Int> get() = _selectedYear
//
//    private val _selectedRuntimeHours = MutableLiveData<Int>()
//    val selectedRuntimeHours: LiveData<Int> get() = _selectedRuntimeHours
//
//    private val _selectedRuntimeMinutes = MutableLiveData<Int>()
//    val selectedRuntimeMinutes: LiveData<Int> get() = _selectedRuntimeMinutes
//
//    private val _selectedImageURI = MutableLiveData<String>()
//    val selectedImageURI: LiveData<String> get() = _selectedImageURI
//
//    private val _isEditMode = MutableLiveData(false)
//    val isEditMode: LiveData<Boolean> get() = _isEditMode

    private val _favorite = MutableLiveData<Boolean>()
    val favorite: LiveData<Boolean> get() = _favorite


    private val _filteredMovies = MutableLiveData<List<Movie>>()
    val filteredMovies: LiveData<List<Movie>> get() = _filteredMovies

//    private val _comedyMovies = MutableLiveData<List<Movie>>()
//    val comedyMovies: LiveData<List<Movie>> = _comedyMovies
//
//    private val _thrillerMovies = MutableLiveData<List<Movie>>()
//    val thrillerMovies: LiveData<List<Movie>> = _thrillerMovies
//
//    private val _fantasyMovies = MutableLiveData<List<Movie>>()
//    val fantasyMovies: LiveData<List<Movie>> = _fantasyMovies
//
//    private val _romanceMovies = MutableLiveData<List<Movie>>()
//    val romanceMovies: LiveData<List<Movie>> = _romanceMovies
//
//    private val _familyMovies = MutableLiveData<List<Movie>>()
//    val familyMovies: LiveData<List<Movie>> = _familyMovies

    private val _recognition = MutableLiveData<String>()
    val recognition : LiveData<String> get() = _recognition


    fun setRecognition(string: String){
        _recognition.value = string
    }


//    fun filterMoviesByGenre(movies: List<Movie>) {
//        _comedyMovies.value = movies.filter { it.genre.contains(R.string.comedy_label) }
//        _thrillerMovies.value = movies.filter { it.genre.contains(R.string.thriller_label) }
//        _fantasyMovies.value = movies.filter { it.genre.contains(R.string.science_fiction_label) }
//        _romanceMovies.value = movies.filter { it.genre.contains(R.string.romance_label) }
//        _familyMovies.value = movies.filter { it.genre.contains(R.string.family_label) }
//    }


    fun setFilteredMovies(movies: List<Movie>) {
        _filteredMovies.value = movies

    }

//    fun setEditMode(isEdit: Boolean) {
//        _isEditMode.value = isEdit
//    }
//
//    fun setSelectedImageURI(uri: String?) {
//        _selectedImageURI.value = uri ?: ""
//    }
//
//    fun setSelectedRuntimeHours(hours: Int) {
//        _selectedRuntimeHours.value = hours
//    }
//
//    fun setSelectedRuntimeMinutes(minutes: Int) {
//        _selectedRuntimeMinutes.value = minutes
//    }
//
//    fun setSelectedYear(year: Int) {
//        _selectedYear.value = year
//    }

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

//    fun clearAllData() {
//        setSelectedYear(0)
//        setSelectedRuntimeHours(0)
//        setSelectedRuntimeMinutes(0)
//        setSelectedImageURI(null)
//        setFavorite(false)
//    }

}