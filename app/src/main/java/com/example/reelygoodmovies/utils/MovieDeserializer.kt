package com.example.reelygoodmovies.utils

import com.example.reelygoodmovies.data.models.Movie
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import java.lang.reflect.Type

class MovieDeserializer : JsonDeserializer<Movie> {
    override fun deserialize(
        json: JsonElement,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): Movie {
        val jsonObject = json.asJsonObject
        val id = jsonObject.get("id").asInt
        val title = jsonObject.get("title").asString
        val overview = jsonObject.get("overview").asString
        val posterPath = jsonObject.get("poster_path")?.asString
        val voteAverage = jsonObject.get("vote_average").asFloat/2
        val movieLength = if(jsonObject.has("runtime")){
            jsonObject.get("runtime").asInt
        } else {
            0
        }
        val releaseDate = jsonObject.get("release_date").asString
        val releaseYear = if (releaseDate.length>1) releaseDate.substring(0,4).toInt() else 0
        val genreIds : List<Int> = if (jsonObject.has("genres")){
            jsonObject.getAsJsonArray("genres").map{
                it.asJsonObject.get("id").asInt
            }
        }
        else if (jsonObject.has("genre_ids")){
            jsonObject.getAsJsonArray("genre_ids").map{
                it.asJsonObject.get("id").asInt
            }
        }
        else {
            emptyList()
        }

        return Movie(
            title = title,
            plot = overview,
            rate = voteAverage,
            genre = genreIds,
            photo = "",
            year = releaseYear,
            length = movieLength,
            id = id
        )
    }
}