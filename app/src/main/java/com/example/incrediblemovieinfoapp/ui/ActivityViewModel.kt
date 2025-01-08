package com.example.incrediblemovieinfoapp.ui

import android.app.Application
import android.content.Context
import androidx.constraintlayout.widget.R
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.map
import com.example.incrediblemovieinfoapp.data.models.Movie
import com.example.incrediblemovieinfoapp.data.repositories.movieRepository



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


    private val _isEditMode = MutableLiveData<Boolean>(false)
    val isEditMode: LiveData<Boolean> get() = _isEditMode

    private var _genres = mutableMapOf(
        "Comedy" to false,
        "Horror" to false,
        "Science Fiction" to false,
        "War" to false,
        "Family" to false,
        "Action" to false,
        "Romance" to false,
        "Animation" to false,
        "Drama" to false,
        "Thriller" to false,
        "Adventure" to false,
        "Doco" to false,
    )
    val genres get() = _genres

    fun setGenres(genreString: String){
        clearGenres()
        for (genre in genreString.split(",")){
            _genres[genre.trim()] = true
        }
    }

    fun setGenres(genreList : List<String>){
        for (genre in genreList){
            _genres[genre.trim()] = true
        }
    }
    fun clearGenres(){
        for (genre in _genres){
            genre.setValue(false)
        }
    }

    fun getGenresAsString() : String    {
        return genres.filter{ it.value }.keys.joinToString (",")
    }

    fun getGeneresAsLocalizedString(context : Context, movie : Movie?) : String { // maybe needed
//        val keyToResourceMap = mapOf(
//            "Comedy" to context.getString(com.example.incrediblemovieinfoapp.R.string.comedy_label),
//            "Horror" to context.getString(com.example.incrediblemovieinfoapp.R.string.horror_label),
//            "Science Fiction" to context.getString(com.example.incrediblemovieinfoapp.R.string.science_fiction_label),
//            "War" to context.getString(com.example.incrediblemovieinfoapp.R.string.war_label),
//            "Family" to context.getString(com.example.incrediblemovieinfoapp.R.string.family_label),
//            "Action" to context.getString(com.example.incrediblemovieinfoapp.R.string.action_label),
//            "Romance" to context.getString(com.example.incrediblemovieinfoapp.R.string.romance_label),
//            "Animation" to context.getString(com.example.incrediblemovieinfoapp.R.string.animation_label),
//            "Drama" to context.getString(com.example.incrediblemovieinfoapp.R.string.drama_label),
//            "Thriller" to context.getString(com.example.incrediblemovieinfoapp.R.string.thriller_label),
//            "Adventure" to context.getString(com.example.incrediblemovieinfoapp.R.string.adventure_label),
//            "Doco" to context.getString(com.example.incrediblemovieinfoapp.R.string.doco_label)
//        )
//
//        val translatedGenres = _genres.filter { it.value }
//            .keys
//            .mapNotNull { keyToResourceMap[it]?.let { resId -> context.getString(resId) } }
//            .joinToString(",")

        var tempGenres = getGenresAsString()

        if (movie!= null){ // Changing the current movie of model in order to take advantage of the functions in the viewmodel
            setGenres(movie.genre)
        }

        val localizedGenres = genres.filter { it.value }  // Only selected genres (true)
            .keys // Get the genre names
            .mapNotNull { genre ->
                when (genre) {
                    "Comedy" -> context.getString(com.example.incrediblemovieinfoapp.R.string.comedy_label)
                    "Horror" -> context.getString(com.example.incrediblemovieinfoapp.R.string.horror_label)
                    "Science Fiction" -> context.getString(com.example.incrediblemovieinfoapp.R.string.science_fiction_label)
                    "War" -> context.getString(com.example.incrediblemovieinfoapp.R.string.war_label)
                    "Family" -> context.getString(com.example.incrediblemovieinfoapp.R.string.family_label)
                    "Action" -> context.getString(com.example.incrediblemovieinfoapp.R.string.action_label)
                    "Romance" -> context.getString(com.example.incrediblemovieinfoapp.R.string.romance_label)
                    "Animation" -> context.getString(com.example.incrediblemovieinfoapp.R.string.animation_label)
                    "Drama" -> context.getString(com.example.incrediblemovieinfoapp.R.string.drama_label)
                    "Thriller" -> context.getString(com.example.incrediblemovieinfoapp.R.string.thriller_label)
                    "Adventure" -> context.getString(com.example.incrediblemovieinfoapp.R.string.adventure_label)
                    "Doco" -> context.getString(com.example.incrediblemovieinfoapp.R.string.doco_label)
                    else -> null
                }
            }
            .joinToString(", ") // Join all localized genres into a single string

        setGenres(tempGenres)
        return localizedGenres
    }

    fun setEditMode(isEdit: Boolean) {
        _isEditMode.value = isEdit
    }

    fun setSelectedImageURI(uri: String?) {
        _selectedImageURI.value = uri ?: ""
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
       setGenres(movie.genre)
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

    fun updateMovie(movie: Movie){
        repository.updateMovie(movie)
    }

    fun clearAllData(){
        setSelectedYear(1900)
        setSelectedRuntimeHours(0)
        setSelectedRuntimeMinutes(0)
        setSelectedImageURI(null)
        clearGenres()
    }
}