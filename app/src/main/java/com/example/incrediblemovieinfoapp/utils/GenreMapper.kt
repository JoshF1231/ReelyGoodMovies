package com.example.incrediblemovieinfoapp.utils

import android.content.Context
import com.example.incrediblemovieinfoapp.data.models.Movie

object GenreMapper {
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

    val genres : Map<String,Boolean> get() = _genres

    fun setGenres(genreString: String){
        clearGenres()
        for (genre in genreString.split(",")){
            _genres[genre.trim()] = true
        }
    }

    fun setGenres(genreList : List<String>){
        clearGenres()
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

    fun getGenresAsLocalizedString(context : Context, movie : Movie?) : String {
        val tempGenres = getGenresAsString()

        if (movie!= null){
            setGenres(movie.genre)
        }

        val localizedGenres = genres.filter { it.value }  // Only selected genres (true)
            .keys
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
            .joinToString(", ")
        setGenres(tempGenres)
        return localizedGenres
    }
}