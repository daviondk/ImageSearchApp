package com.`is`.daviondk.imagesearchapp.images

import android.os.AsyncTask
import android.util.Log
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import java.lang.ref.WeakReference
import java.net.HttpURLConnection
import java.net.URL
import java.util.*
import kotlin.math.min

object ImageContent {

    val ITEMS: MutableList<ImageItem> = ArrayList()

    val ITEM_MAP: MutableMap<String, ImageItem> = HashMap()

    private const val COUNT = 15

    init {
        val downloadTask = DownloadPicturesAsyncTask(WeakReference(this))
        downloadTask.execute(URL("https://api.unsplash.com/search/photos/?query=cat&per_page=50&client_id=72fb6fd978959ab637fcc4d9f89a055b8849aad66c28a9aace5c376bbee3883c"))
        val pictureJson = downloadTask.get()
        val array = pictureJson.getAsJsonArray("results")
        for (i in 0..min(array.size(), COUNT - 1)) {
            val description = array[i].asJsonObject.get("description").asString
            val download = array[i].asJsonObject.getAsJsonObject("links").get("download").asString
            val preview = array[i].asJsonObject.getAsJsonObject("urls").get("thumb").asString
            ITEMS.add(ImageItem(description, download, preview))
            ITEM_MAP[description] = ImageItem(description, download, preview)
        }
    }

    data class ImageItem(val description: String, val download_link: String, val preview: String)

    private class DownloadPicturesAsyncTask(val activity: WeakReference<ImageContent>) : AsyncTask<URL, Unit, JsonObject>() {

        private val logTag = "ASYNC_TASK"
        private lateinit var jsonResponse: JsonObject

        override fun doInBackground(vararg params: URL): JsonObject {
            Log.d(logTag, "Downloading from ${params[0]}")

            val response = params[0].openConnection().run {
                Log.d(logTag, "Opened Connection")
                connect()
                Log.d(logTag, "Connected")
                val code = (this as? HttpURLConnection)?.responseCode
                Log.d(logTag, "Response code: $code")

                getInputStream().bufferedReader().readLines().joinToString("")
            }
            val js = JsonParser().parse(response)
            jsonResponse = js.asJsonObject
            Log.d(logTag, "Response = $jsonResponse")
            return jsonResponse
        }

    }
}
