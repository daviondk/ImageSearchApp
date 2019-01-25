package com.`is`.daviondk.imagesearchapp

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
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
        setupPermissions()
    }

    private fun hasPermissions(context: Context?, permissions: Array<String>): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
            for (permission in permissions) {
                if (ActivityCompat.checkSelfPermission(context!!, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false
                }
            }
        }
        return true
    }

    private fun getPermissionToast() {
        val toast = Toast.makeText(applicationContext, "You can't load hi-res without permissions", Toast.LENGTH_SHORT)
        toast.setGravity(Gravity.CENTER, 0, 0)
        toast.show()
    }

    private fun setupPermissions() {
        val mContext = this

        val REQUEST = 112

        if (Build.VERSION.SDK_INT >= 23) {
            val PERMISSIONS = arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
            if (!hasPermissions(mContext, PERMISSIONS)) {
                ActivityCompat.requestPermissions(mContext as Activity, PERMISSIONS, REQUEST)
            }
        }
    }

    private fun setupRecyclerView(recyclerView: RecyclerView) {

        recyclerView.layoutManager = LinearLayoutManager(this)  //--???
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
