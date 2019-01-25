package com.`is`.daviondk.imagesearchapp

import android.app.Service
import android.content.Intent
import android.os.Environment
import android.os.IBinder
import android.util.Log
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.net.URL

class LoaderService : Service() {
    private val LOG_TAG = "serviceLogs"
    private var imageName = ""
    private var curThread: Thread? = null
    private var downloadedFlag = false

    override fun onCreate() {
        super.onCreate()
        Log.d(LOG_TAG, "onCreateService")
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        val thread = Thread(Runnable { downloadTask(intent) })
        curThread = thread
        thread.start()
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onDestroy() {
        stopDownload()
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

        if (!imageIsExist(imageName)) {
            load(url)
        } else {
            val int = Intent(ImageDetailFragment.BROADCAST_ACTION)
            int.putExtra(ImageDetailFragment.PARAM_STATUS, ImageDetailFragment.STATUS_FINISH)
            int.putExtra(ImageDetailFragment.IMAGE_NAME, imageName)
            sendBroadcast(int)
            downloadedFlag = true
            stopSelf()
        }
    }

    private fun load(url: String?) {
        URL(url).openConnection().run {
            val byteArrayOutputStream = ByteArrayOutputStream()
            getInputStream().copyTo(byteArrayOutputStream)
            val file = File(Environment.getExternalStorageDirectory().path + "/" + imageName)
            file.createNewFile()
            FileOutputStream(file).use({ outputStream -> byteArrayOutputStream.writeTo(outputStream) })
            val int = Intent(ImageDetailFragment.BROADCAST_ACTION)
            int.putExtra(ImageDetailFragment.PARAM_STATUS, ImageDetailFragment.STATUS_FINISH)
            int.putExtra(ImageDetailFragment.IMAGE_NAME, imageName)
            sendBroadcast(int)
            downloadedFlag = true
            stopSelf()
        }
    }

    fun stopDownload() {
        if (!downloadedFlag) {
            curThread?.interrupt()
            val file = File(Environment.getExternalStorageDirectory().path + "/" + imageName)
            if (file.exists()) {
                file.delete()
            }
        }
    }
}