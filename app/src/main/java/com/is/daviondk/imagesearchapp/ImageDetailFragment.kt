package com.`is`.daviondk.imagesearchapp

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.os.Environment
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.`is`.daviondk.imagesearchapp.images.ImageContent
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.image_detail.view.*
import java.io.File


class ImageDetailFragment : Fragment() {
    private var item: ImageContent.ImageItem? = null

    private lateinit var imageView: ImageView
    private lateinit var link: String

    private val LOG_TAG = "fragmentLogs"

    lateinit var br: BroadcastReceiver

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            if (it.containsKey(ARG_ITEM_ID)) {
                item = ImageContent.ITEM_MAP[it.getString(ARG_ITEM_ID)]
            }
        }

        br = object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) {
                val status = intent.getIntExtra(PARAM_STATUS, 0)

                if (status == STATUS_FINISH) {
                    setImage()
                }
            }
        }
        val intFilt = IntentFilter(BROADCAST_ACTION)
        activity?.registerReceiver(br, intFilt)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.image_detail, container, false)

        item?.let {
            ContextCompat.checkSelfPermission(this.context!!, "INTERNET")

            link = it.download_link
            imageView = rootView.image_detail
            imageView.setImageResource(R.drawable.progress_animation)
            val intent = Intent(context, LoaderService::class.java)
            context?.startService(intent.putExtra("url", link))
        }

        return rootView
    }

    override fun onDestroy() {
        activity?.unregisterReceiver(br)
        super.onDestroy()
    }

    companion object {
        const val ARG_ITEM_ID = "item_id"
        const val BROADCAST_ACTION = "servicebackbroadcast"
        const val STATUS_FINISH = 1
        const val PARAM_STATUS = "status"
    }

    fun setImage() {
        val name = link.hashCode().toString()
        val myImageFile = File(Environment.getExternalStorageDirectory().path + "/" + name)
        Log.d(LOG_TAG, "Path for file: " + myImageFile.absolutePath.toString())
        Picasso.get().load(myImageFile)
                .resize(2048, 2048)
                .onlyScaleDown()
                .centerInside()
                .placeholder(R.drawable.progress_animation)
                .into(imageView)
        Log.d(LOG_TAG, "Image set")
    }
}
