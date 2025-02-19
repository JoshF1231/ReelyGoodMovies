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

    fun getMovies() = performFetchingAndSaving(
        { localDataSource.getMovies() },
        { remoteDataSource.getMovies() },
        { moviesFromApi ->
            val favoriteMovies = localDataSource.getFavoriteMoviesSync()
            localDataSource.addMovies(moviesFromApi.results)
            favoriteMovies.forEach{movie -> localDataSource.updateMovie(movie)}
        }
    )


    fun getMovie(id: Int) = performFetchingAndSaving(
        { localDataSource.getMovie(id) },
        { remoteDataSource.getMovie(id) },
        { movieFromApi ->
            val isFavoriteMovie = localDataSource.getFavoriteMovieById(id)
            movieFromApi.favorite = isFavoriteMovie != null
            localDataSource.updateMovie(movieFromApi)
        }
    )

}
