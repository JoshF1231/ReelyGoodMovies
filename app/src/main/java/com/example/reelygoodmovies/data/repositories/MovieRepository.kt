package com.example.reelygoodmovies.data.repositories

import com.example.reelygoodmovies.data.local_db.MovieDao
import com.example.reelygoodmovies.data.models.Movie
import com.example.reelygoodmovies.data.remote_db.MovieRemoteDataSource
import com.example.reelygoodmovies.utils.performFetchingAndSaving
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MovieRepository @Inject constructor(
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


    suspend fun addMovie(movie: Movie) {
        localDataSource.addMovie(movie)
    }

    suspend fun updateMovie(movie: Movie) {
        localDataSource.updateMovie(movie)
    }

    suspend fun deleteMovie(movie: Movie) {
        localDataSource.deleteMovie(movie)
    }

    suspend fun deleteAllMovies() {
        localDataSource.deleteAllMovies()
    }

    fun getFavoriteMovies() = localDataSource.getFavoriteMovies()

}
