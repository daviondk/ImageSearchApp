package com.`is`.daviondk.imagesearchapp

import kotlinx.coroutines.Deferred
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

data class PhotoResponse(
        val results: List<Photo>
)

interface UnsplashApi{
    @GET("search/photos?per_page=20&client_id=249334b832e8bab027237337fe1a805eea155eec893d57f1f95a71d17ca6bf53")

    fun getPhotos(@Query("query") name: String): Deferred<Response<PhotoResponse>>
}