package com.`is`.daviondk.imagesearchapp

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.`is`.daviondk.imagesearchapp.images.ImageContent
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.image_detail.view.*

class ImageDetailFragment : Fragment() {
    private var item: ImageContent.ImageItem? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            if (it.containsKey(ARG_ITEM_ID)) {
                item = ImageContent.ITEM_MAP[it.getString(ARG_ITEM_ID)]
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.image_detail, container, false)

        item?.let {
            ContextCompat.checkSelfPermission(this.context!!, "INTERNET")
            Picasso.get().load(it.download_link).into(rootView.image_detail)
        }

        return rootView
    }

    companion object {
        const val ARG_ITEM_ID = "item_id"
    }
}
