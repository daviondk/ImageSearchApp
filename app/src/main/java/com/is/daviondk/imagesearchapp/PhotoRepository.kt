package com.`is`.daviondk.imagesearchapp

class PhotoRepository(private val api : UnsplashApi) : BaseRepository() {

    suspend fun getPhotos(name : String) : MutableList<Photo>?{
//suspend??????????
        //safeApiCall is defined in BaseRepository.kt (https://gist.github.com/navi25/67176730f5595b3f1fb5095062a92f15)
        val vkPhotoResponse = safeApiCall(
                call = {api.getPhotos(name).await()},
                errorMessage = "Error Fetching Photos"
        )

        return vkPhotoResponse?.results?.toMutableList();

    }

}