package com.`is`.daviondk.imagesearchapp

import android.arch.persistence.room.TypeConverter
import java.util.Arrays.asList


class UrlsConverter {

    @TypeConverter
    fun fromUrls(urls: Photo.Urls): String {
        return urls.full + "#" + urls.thumb
    }

    @TypeConverter
    fun toUrls(data: String): Photo.Urls {
        val urls = data.split("#")
        return Photo.Urls(urls.get(0), urls.get(1))
    }

}