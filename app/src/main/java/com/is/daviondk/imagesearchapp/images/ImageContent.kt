package com.`is`.daviondk.imagesearchapp.images

import android.os.AsyncTask
import android.util.Log
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import java.net.URL
import java.util.*
import kotlin.math.min

object ImageContent {

    val ITEMS: MutableList<ImageItem> = ArrayList()
    val ITEM_MAP: MutableMap<String, ImageItem> = HashMap()

    private val LOG_TAG = "imageLogs"
    private const val COUNT = 20

    init {
        val downloadImagesTask = DownloadImagesAsyncTask()
        downloadImagesTask.execute(URL("https://api.unsplash.com/search/photos/?query=cat&per_page=50&client_id=72fb6fd978959ab637fcc4d9f89a055b8849aad66c28a9aace5c376bbee3883c"))
        val jsonObject = downloadImagesTask.get()
        val jsonArray = jsonObject.getAsJsonArray("results")
        for (i in 0..min(jsonArray.size(), COUNT) - 1) {
            var preview = ""
            var download = ""
            var description = ""
            try {
                preview = jsonArray[i].asJsonObject.getAsJsonObject("urls").get("thumb").asString
                download = jsonArray[i].asJsonObject.getAsJsonObject("links").get("download").asString
                description = jsonArray[i].asJsonObject.get("description").asString
            } catch (e : UnsupportedOperationException) { Log.d(LOG_TAG, "Missing json option: " + e.printStackTrace()) }
            ITEMS.add(ImageItem(description, download, preview))
            ITEM_MAP[description] = ImageItem(description, download, preview)
        }
    }

    data class ImageItem(val description: String, val download_link: String, val preview: String)

    private class DownloadImagesAsyncTask : AsyncTask<URL, Unit, JsonObject>() {

        private val LOG_TAG = "gettingJsonResonse"
        private lateinit var jsonResult: JsonObject

        override fun doInBackground(vararg params: URL): JsonObject {
            Log.d(LOG_TAG, "Downloading from ${params[0]}")

            val response = params[0].openConnection().run {
                Log.d(LOG_TAG, "Opened Connection")
                connect()
                Log.d(LOG_TAG, "Connected")
                getInputStream().bufferedReader().readLines().joinToString("")
            }
            val js = JsonParser().parse(response)
            jsonResult = js.asJsonObject
            Log.d(LOG_TAG, "Response = $jsonResult")
            return jsonResult
        }

    }
}
