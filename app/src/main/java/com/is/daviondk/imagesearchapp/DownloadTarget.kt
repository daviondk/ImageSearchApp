package com.`is`.daviondk.imagesearchapp

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Environment
import android.util.Log
import com.squareup.picasso.Picasso
import com.squareup.picasso.Target
import java.io.File
import java.io.FileOutputStream

class DownloadTarget(private val name: String, private val loader: LoaderService) : Target {
    val LOG_TAG = "targetLogs"

    override fun onPrepareLoad(placeHolderDrawable: Drawable?) {
        Log.d(LOG_TAG, "Prepare to load")
    }

    override fun onBitmapFailed(e: java.lang.Exception?, errorDrawable: Drawable?) {
        Log.d(LOG_TAG, "Bitmap load failed")
        loader.stopSelf()
    }

    override fun onBitmapLoaded(bitmap: Bitmap, arg1: Picasso.LoadedFrom) {
        Log.d(LOG_TAG, "Bitmap loaded")
        val file = File(Environment.getExternalStorageDirectory().path + "/" + name)
        try {
            Thread(Runnable {
                file.createNewFile()
                val ostream = FileOutputStream(file)
                bitmap.compress(Bitmap.CompressFormat.JPEG, 75, ostream)
                Log.d(LOG_TAG, "Bitmap set")
                ostream.close()
                val int = Intent(ImageDetailFragment.BROADCAST_ACTION)
                int.putExtra(ImageDetailFragment.PARAM_STATUS, ImageDetailFragment.STATUS_FINISH)
                int.putExtra(ImageDetailFragment.IMAGE_NAME, name)
                loader.sendBroadcast(int)
                Log.d(LOG_TAG, "Info broadcasted for image set")
                loader.stopSelf()
            }).start()
        } catch (e: Exception) {
            Log.d(LOG_TAG, "Download failed with exception: " + e.printStackTrace())
            loader.stopSelf()
        }
    }
}
