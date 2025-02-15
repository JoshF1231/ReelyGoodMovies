package com.example.reelygoodmovies.data.remote_db

import com.example.reelygoodmovies.data.models.AllMovies
import com.example.reelygoodmovies.data.models.Movie
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface MovieService {
    @GET("movie")
    suspend fun getAllMovies() : Response<AllMovies>

    @GET("movie/{id}")
    suspend fun getMovie(@Path("id")id :Int) : Response<Movie>
}