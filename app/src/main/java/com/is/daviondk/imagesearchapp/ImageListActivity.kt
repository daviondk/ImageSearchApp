package com.`is`.daviondk.imagesearchapp

import android.content.Intent
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.`is`.daviondk.imagesearchapp.images.ImageContent
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_image_list.*
import kotlinx.android.synthetic.main.image_list.*
import kotlinx.android.synthetic.main.image_list_content.view.*

class ImageListActivity : AppCompatActivity() {

    private var twoPane: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image_list)

        setSupportActionBar(toolbar)
        toolbar.title = title

        if (image_detail_container != null) {
            twoPane = true
        }

        setupRecyclerView(image_list)
    }

    private fun setupRecyclerView(recyclerView: RecyclerView) {
        recyclerView.adapter = SimpleItemRecyclerViewAdapter(this, ImageContent.ITEMS, twoPane)
    }

    class SimpleItemRecyclerViewAdapter(private val parentActivity: ImageListActivity,
                                        private val values: List<ImageContent.ImageItem>,
                                        private val twoPane: Boolean) :
            RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder>() {

        private val onClickListener: View.OnClickListener

        init {
            onClickListener = View.OnClickListener { v ->
                val item = v.tag as ImageContent.ImageItem
                if (twoPane) {
                    val fragment = ImageDetailFragment().apply {
                        arguments = Bundle().apply {
                            putString(ImageDetailFragment.ARG_ITEM_ID, item.description)
                        }
                    }
                    parentActivity.supportFragmentManager
                            .beginTransaction()
                            .replace(R.id.image_detail_container, fragment)
                            .commit()
                } else {
                    val intent = Intent(v.context, ImageDetailActivity::class.java).apply {
                        putExtra(ImageDetailFragment.ARG_ITEM_ID, item.description)
                    }
                    v.context.startActivity(intent)
                }
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.image_list_content, parent, false)
            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val item = values[position]
            holder.contentView.text = item.description

            ContextCompat.checkSelfPermission(parentActivity, "INTERNET")
            Picasso.get().load(item.preview).into(holder.imageView)
            with(holder.itemView) {
                tag = item
                setOnClickListener(onClickListener)
            }
        }

        override fun getItemCount() = values.size

        inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            val imageView: ImageView = view.imageView2
            val contentView: TextView = view.content
        }
    }
}
