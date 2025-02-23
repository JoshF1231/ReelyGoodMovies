package com.example.reelygoodmovies.data.repositories

import com.example.reelygoodmovies.data.local_db.MovieDao
import com.example.reelygoodmovies.data.models.Movie
import com.example.reelygoodmovies.data.remote_db.MovieRemoteDataSource
import com.example.reelygoodmovies.utils.performFetchingAndSaving
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MovieRepository @Inject constructor(
    private val remoteDataSource: MovieRemoteDataSource,
    private val localDataSource: MovieDao
) {

    fun getMovies() = performFetchingAndSaving(
        { localDataSource.getMovies() },
        { remoteDataSource.getMovies() },
        { moviesFromApi ->
            val favoriteMovies = localDataSource.getFavoriteMoviesSync()
            localDataSource.addMovies(moviesFromApi.results)
            favoriteMovies.forEach { movie -> localDataSource.updateMovie(movie) }
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

    fun getMovieTrailer(id: Int) = performFetchingAndSaving(
        { localDataSource.getMovieTrailer(id) },
        { remoteDataSource.getMovieTrailer(id) },
        { trailerResponse ->
            val trailerUrl = trailerResponse.results
                .firstOrNull { it.type == "Trailer" }
                ?.key
            if (trailerUrl != null) {
                localDataSource.updateMovieTrailer(id, trailerUrl)
            }
        }
    )

    //    WORK IN PROGRESS - JOSH
//    fun getMovie(id: Int): LiveData<Resource<Movie>> = liveData(Dispatchers.IO) {
//        val localMovie = localDataSource.getMovie(id).value // Get movie from local DB
//        if (localMovie != null) {
//            emit(Resource.success(localMovie)) //Emit a copy of the local data
//        }
//        if (localMovie == null || !localMovie.localGen) { // If not locally generated, attempt to retrieve from remote
//            performFetchingAndSaving(
//                { localDataSource.getMovie(id) },
//                { remoteDataSource.getMovie(id) },
//                { movieFromApi ->
//                    val isFavoriteMovie = localDataSource.getFavoriteMovieById(id)
//                    movieFromApi.favorite = isFavoriteMovie != null
//                    localDataSource.updateMovie(movieFromApi)
//                }
//            )
//        }
//    }

    suspend fun addMovie(movie: Movie) {
        localDataSource.addMovie(movie)
    }

    suspend fun updateMovie(movie: Movie) {
        localDataSource.updateMovie(movie)
    }

    suspend fun deleteMovie(movie: Movie) {
        localDataSource.deleteMovie(movie)
    }

    fun getFavoriteMovies() = localDataSource.getFavoriteMovies()
}
