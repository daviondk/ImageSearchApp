package com.`is`.daviondk.imagesearchapp

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.image_detail.view.*


class ImageDetailFragment : Fragment() {
    private var url: String? = null
    private val LOG_TAG = "fragmentLogs"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            if (it.containsKey(ITEM_URL)) {
                url = it.getString(ITEM_URL)
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.image_detail, container, false)

        ContextCompat.checkSelfPermission(this.context!!, "INTERNET")
        rootView.image_detail.setImageResource(R.drawable.progress_animation)

        Picasso.get().load(url)
                .resize(2048, 2048)
                .onlyScaleDown()
                .centerInside()
                .placeholder(R.drawable.progress_animation)
                .into(rootView.image_detail)
        return rootView
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    companion object {
        const val ITEM_URL = "cur_item_url"
    }

}
