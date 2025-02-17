package com.example.reelygoodmovies.utils

import com.example.reelygoodmovies.data.local_db.MovieDao
import com.example.reelygoodmovies.data.remote_db.MovieRemoteDataSource
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MovieRepositoryNew @Inject constructor(
    private val remoteDataSource : MovieRemoteDataSource,
    private val localDataSource : MovieDao
) {

    fun getMovies () = performFetchingAndSaving(
        {localDataSource.getMovies()},
        {remoteDataSource.getMovies()},
        {localDataSource.addMovies(it.results)}
    )
    fun getMovie (id : Int) = performFetchingAndSaving(
        {localDataSource.getMovie(id)},
        {remoteDataSource.getMovie(id)},
        {localDataSource.addMovie(it)}
    )
}