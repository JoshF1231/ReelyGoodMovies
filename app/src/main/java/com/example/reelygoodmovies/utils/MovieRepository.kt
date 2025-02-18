//package com.example.reelygoodmovies.utils
//
//import com.example.reelygoodmovies.data.local_db.MovieDao
//import javax.inject.Inject
//import javax.inject.Singleton
//
//@Singleton
//class MovieRepository @Inject constructor(
//    private val remoteDataSource : MovieRemoteDataSource,
//    private val localDataSource : MovieDao
//) {
//
//    fun getMovies () = performFetchingAndSaving(
//        {localDataSource.getMovies()},
//        {remoteDataSource.getMovies()},
//        {localDataSource.addMoviesCheckDuplicate()}
//    )
//    fun getMovie (id : Int) = performFetchingAndSaving(
//        {localDataSource.getMovie(id)},
//        {remoteDataSource.getMovieById(id)},
//        {localDataSource.addMovieCheckDuplicate(it)}
//    )
//}