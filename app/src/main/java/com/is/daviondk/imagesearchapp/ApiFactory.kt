package com.`is`.daviondk.imagesearchapp

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

object ApiFactory {

    private val authInterceptor = Interceptor { chain ->
        val newUrl = chain.request().url()
                .newBuilder()
                .addQueryParameter("api_key", "c1a37bb988f47171570e93ee2e837d036ffc989419a28cbfe6ac377999418edb")
                .build()

        val newRequest = chain.request()
                .newBuilder()
                .url(newUrl)
                .build()

        chain.proceed(newRequest)
    }

    private val unsplashClient = OkHttpClient().newBuilder()
            .addInterceptor(authInterceptor)
            .build()


    fun retrofit(): Retrofit = Retrofit.Builder()
            .client(unsplashClient)
            .baseUrl("https://api.unsplash.com/")
            .addConverterFactory(MoshiConverterFactory.create())
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .build()


    val unsplashApi: UnsplashApi = retrofit().create(UnsplashApi::class.java)

}