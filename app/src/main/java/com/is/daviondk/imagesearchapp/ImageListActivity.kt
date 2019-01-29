package com.`is`.daviondk.imagesearchapp

import android.app.Activity
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
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
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_image_list.*
import kotlinx.android.synthetic.main.image_list.*
import kotlinx.android.synthetic.main.image_list_content.view.*

class ImageListActivity : AppCompatActivity() {

    private var twoPane: Boolean = false

    private lateinit var photoViewModel: PhotoViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image_list)

        setSupportActionBar(toolbar)
        toolbar.title = title

        if (image_detail_container != null) {
            twoPane = true
        }

        setupPermissions()
        setupRecyclerView(image_list, "random")

        val searchButton = findViewById<Button>(R.id.searchButton)
        val searchField = findViewById<EditText>(R.id.usernameField)

        searchButton.setOnClickListener {
            setupRecyclerView(image_list, searchField.text.toString())
        }
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

    private fun setupRecyclerView(recyclerView: RecyclerView, query: String) {

        recyclerView.layoutManager = LinearLayoutManager(this)  //--???

        photoViewModel = ViewModelProviders.of(this).get(PhotoViewModel::class.java)

        photoViewModel.fetchPhotos(query)

        photoViewModel.photosLiveData.observe(this, Observer {
            if (it != null) {
                recyclerView.adapter = SimpleItemRecyclerViewAdapter(this, it, photoViewModel, twoPane)
            }
        })

    }

    class SimpleItemRecyclerViewAdapter(private val parentActivity: ImageListActivity,
                                        private val values: MutableList<Photo>,
                                        private val viewModel: PhotoViewModel,
                                        private val twoPane: Boolean) :
            RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder>() {

        private val onClickListener: View.OnClickListener

        init {
            onClickListener = View.OnClickListener { v ->
                val item = v.tag as Photo
                if (twoPane) {
                    val fragment = ImageDetailFragment().apply {
                        arguments = Bundle().apply {
                            putString(ImageDetailFragment.ITEM_URL, item.urls?.full)
                        }
                    }
                    parentActivity.supportFragmentManager
                            .beginTransaction()
                            .replace(R.id.image_detail_container, fragment)
                            .commit()
                } else {
                    val intent = Intent(v.context, ImageDetailActivity::class.java).apply {
                        putExtra(ImageDetailFragment.ITEM_URL, item.urls?.full)
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
            Picasso.get().load(item.urls?.thumb).into(holder.imageView)
            with(holder.itemView) {
                tag = item
                setOnClickListener(onClickListener)
            }

            holder.checkBox.isChecked = viewModel.isFavourite(item)
            holder.checkBox.setOnCheckedChangeListener { buttonView, isChecked ->
                if (isChecked) {
                    viewModel.addFavourite(item)
                } else {
                    viewModel.removeFavourite(item)
                }
            }

        }

        override fun getItemCount() = values.size
        inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            val imageView: ImageView = view.imageView2
            val contentView: TextView = view.content
            val checkBox: CheckBox = view.favourite
        }
    }

    companion object {
        private const val LOG_TAG: String = "ImageList"
    }
}
