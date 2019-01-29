package com.`is`.daviondk.imagesearchapp

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import kotlinx.android.synthetic.main.activity_image_detail.*

class ImageDetailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image_detail)
        setSupportActionBar(detail_toolbar)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        if (savedInstanceState == null) {
            val fragment = ImageDetailFragment().apply {
                arguments = Bundle().apply {
                    putString(ImageDetailFragment.ITEM_URL,
                            intent.getStringExtra(ImageDetailFragment.ITEM_URL))
                }
            }

            supportFragmentManager.beginTransaction()
                    .add(R.id.image_detail_container, fragment)
                    .commit()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem) =
            when (item.itemId) {
                android.R.id.home -> {
                    navigateUpTo(Intent(this, ImageListActivity::class.java))
                    true
                }
                else -> super.onOptionsItemSelected(item)
            }

}
