package com.`is`.daviondk.imagesearchapp

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.util.Log
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class PhotoViewModel : ViewModel() {

    private val parentJob = Job()

    private val coroutineContext: CoroutineContext
        get() = parentJob + Dispatchers.Default

    private val scope = CoroutineScope(coroutineContext)

    private val repository: PhotoRepository = PhotoRepository(ApiFactory.unsplashApi)

    val photosLiveData = MutableLiveData<MutableList<Photo>>()

    fun fetchPhotos(name: String) {
        scope.launch {
            val photos = repository.getPhotos(name)
            photosLiveData.postValue(photos)
        }
    }

    val db = App.instance.getDatabase()
    val photoDao = db?.photoDao()

    val favouriteLiveData = MutableLiveData<MutableList<Photo>>()

    fun fetchFavourite() {
        scope.launch {
            val photos = photoDao?.getAll() as MutableList
            favouriteLiveData.postValue(photos)
        }
    }

    fun addFavourite (photo: Photo) {
        scope.launch {
            if(!isFavourite(photo)) {
                photoDao?.insert(photo)
            }
        }
    }
    fun removeFavourite (photo: Photo) {
        scope.launch {
            if(isFavourite(photo)) {
                photoDao?.delete(photo)
            }
        }
    }
    fun isFavourite (photo: Photo): Boolean {
        var flag = false
        scope.launch {
            flag = (photoDao?.exist(photo.id)!!)
        }
        return flag
    }

    fun cancelAllRequests() = coroutineContext.cancel()
}