package com.`is`.daviondk.imagesearchapp

import android.app.Service
import android.content.Intent
import android.os.Environment
import android.os.IBinder
import android.util.Log
import com.squareup.picasso.Picasso
import java.io.File

class LoaderService : Service() {
    private val LOG_TAG = "serviceLogs"
    private var imageName = ""
    private var dw: DownloadTarget? = null

    override fun onCreate() {
        super.onCreate()
        Log.d(LOG_TAG, "onCreateService")
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        downloadTask(intent)
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onDestroy() {
        Picasso.get().cancelRequest(dw!!)
        super.onDestroy()
        Log.d(LOG_TAG, "onDestroyService")
    }

    override fun onBind(intent: Intent): IBinder? {
        Log.d(LOG_TAG, "onBindService")
        return null
    }

    fun downloadTask(intent: Intent) {
        Log.d(LOG_TAG, "Download task started")
        try {
            downloadImage(intent.getStringExtra("url"))
        } catch (e: InterruptedException) {
            e.printStackTrace()
            stopSelf()
        }
    }

    fun imageIsExist(imageName: String): Boolean {
        val myImageFile = File(Environment.getExternalStorageDirectory().path + "/" + imageName)
        if (myImageFile.exists()) {
            Log.d(LOG_TAG, "File exists")
            return true
        } else {
            Log.d(LOG_TAG, "File not exists")
            return false
        }
    }

    fun downloadImage(url: String) {
        imageName = url.hashCode().toString()
        dw = DownloadTarget(imageName, this)

        if (!imageIsExist(imageName)) {
            Picasso.get()
                    .load(url)
                    .into(dw!!)
        } else {
            val int = Intent(ImageDetailFragment.BROADCAST_ACTION)
            int.putExtra(ImageDetailFragment.PARAM_STATUS, ImageDetailFragment.STATUS_FINISH)
            int.putExtra(ImageDetailFragment.IMAGE_NAME, imageName)
            sendBroadcast(int)
        }
    }
}