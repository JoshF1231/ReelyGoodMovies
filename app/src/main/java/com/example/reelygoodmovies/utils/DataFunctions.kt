package com.example.reelygoodmovies.utils

import androidx.lifecycle.LiveData
import kotlinx.coroutines.Dispatchers
import androidx.lifecycle.liveData
import androidx.lifecycle.map

fun <T,A> performFetchingAndSaving(localDbFetch : () -> LiveData<T>,
                                   remoteDbFetch: suspend () -> Resource <A>,
                                   localDbSave: suspend (A) -> Unit) : LiveData<Resource<T>> =
    liveData (Dispatchers.IO){
        emit(Resource.loading())
        val source = localDbFetch().map { Resource.success(it) }
        emitSource(source)

        val fetchResource = remoteDbFetch()

        if (fetchResource.status is Success)
            localDbSave(fetchResource.status.data!!) //Currently causes
            // an error because the database fields are different from the json

        else if (fetchResource.status is Error){
            emit(Resource.error(fetchResource.status.message))
            emitSource(source)
        }
    }