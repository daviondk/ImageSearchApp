package com.`is`.daviondk.imagesearchapp

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import android.arch.persistence.room.TypeConverters
import android.support.annotation.NonNull

@Entity
class Photo {

    @NonNull
    @PrimaryKey
    var id: String? = null

    var description: String? = null

    @TypeConverters(UrlsConverter::class)
    var urls: Urls? = null

    class Urls(fullConv: String, thumbConv: String) {
        var full: String? = null
        var thumb: String? = null
    }
}