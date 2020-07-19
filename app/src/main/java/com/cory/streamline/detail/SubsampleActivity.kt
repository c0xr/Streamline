package com.cory.streamline.detail

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import com.cory.streamline.R
import com.davemorrissey.labs.subscaleview.ImageSource
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView
import java.io.File
import kotlin.reflect.KClass

private const val ARG_URL = "url"

class SubsampleActivity : AppCompatActivity() {
    companion object {
        fun newIntent(context: Context, fullSizeUrl: String) =
            Intent(context, SubsampleActivity::class.java).apply {
                putExtra(ARG_URL, fullSizeUrl)
            }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_subsample)
        val scaleView = findViewById<SubsamplingScaleImageView>(R.id.scaleView)
        val fullSizeUrl = intent.getStringExtra(ARG_URL)
        Thread() {
            val file: File = Glide.with(this)
                .asFile()
                .load(fullSizeUrl)
                .submit()
                .get()
            this.runOnUiThread {
                scaleView.setImage(ImageSource.uri(file.path))
            }
        }.start()
    }
}