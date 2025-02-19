package com.example.reelygoodmovies.ui.add_edit_movie

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.switchMap
import com.example.reelygoodmovies.data.models.Movie
import com.example.reelygoodmovies.utils.Error
import com.example.reelygoodmovies.utils.Loading
import com.example.reelygoodmovies.utils.MovieRepositoryNew
import com.example.reelygoodmovies.utils.Resource
import com.example.reelygoodmovies.utils.Success
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class EditViewModel @Inject constructor(movieRepositoryNew : MovieRepositoryNew) : ViewModel() {

    private val _id = MutableLiveData<Int>()
    private val _movie = _id.switchMap { id ->
        if (id > 0) {
            movieRepositoryNew.getMovie(id)
        } else {
            liveData { emit(Resource.error("Invalid ID")) }
        }
    }
    val movie: LiveData<Resource<Movie>> = _movie

    fun setId(id: Int) {
        _id.value = id
    }

    private val _favorite = MutableLiveData<Boolean>()
    val favorite: LiveData<Boolean> get() = _favorite

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

    fun clearAllData() {
        setSelectedYear(0)
        setSelectedRuntimeHours(0)
        setSelectedRuntimeMinutes(0)
        setSelectedImageURI(null)
        setFavorite(false)
    }

    fun setFavorite(b: Boolean) {
        _favorite.value = b
    }


    fun updateMovieFields (){
        when (movie.value?.status){
            is Error -> return
            is Loading -> return
            is Success -> {
                clearAllData()
                setSelectedYear(movie.value!!.status.data!!.year)
                setSelectedRuntimeHours(movie.value!!.status.data!!.length%60)
                setSelectedRuntimeMinutes(movie.value!!.status.data!!.length/60)
                setSelectedImageURI(movie.value!!.status.data!!.photo)
                setFavorite(movie.value!!.status.data!!.favorite)
            }
            else -> return
        }
    }
}