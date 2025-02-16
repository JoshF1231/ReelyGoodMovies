package com.example.reelygoodmovies.data.remote_db

import com.example.reelygoodmovies.utils.Resource
import retrofit2.Response

abstract class BaseDataSource {
    protected suspend fun  <T>
            getResult(call : suspend () -> Response<T>) : Resource<T> {
        try{
            val result = call()
            if (result.isSuccessful){
                val body = result.body()
                if (body != null) return Resource.success(body)
            }
            return Resource.error("Network call has failed: " +
            "${result.message()} ${result.code()}")
        } catch (e: Exception){
            return Resource.error("Network call has failed: " +
                    (e.localizedMessage ?: e.toString()))
        }
    }
}