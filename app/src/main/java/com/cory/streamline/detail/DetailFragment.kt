package com.cory.streamline.detail

import android.media.MediaScannerConnection
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import androidx.core.util.TimeUtils
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomViewTarget
import com.cory.streamline.R
import com.cory.streamline.model.web.ImageSource
import com.cory.streamline.util.log
import com.cory.streamline.util.toast
import java.io.File
import java.time.Year
import java.util.*

private const val ARG_TRANSITION_NAME = "transitionName"
private const val ARG_IMAGE_SOURCE = "imageSource"

class DetailFragment : Fragment() {
    private lateinit var transitionName: String
    private lateinit var imageSource: ImageSource
    private var ioThread: Thread? = null

    companion object {
        fun newInstance(transitionName: String, imageSource: ImageSource) =
            DetailFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_TRANSITION_NAME, transitionName)
                    putSerializable(ARG_IMAGE_SOURCE, imageSource)
                }
            }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            transitionName = it.getString(ARG_TRANSITION_NAME)!!
            imageSource = it.getSerializable(ARG_IMAGE_SOURCE) as ImageSource
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val v = inflater.inflate(R.layout.fragment_detail, container, false)
        val imageView = v.findViewById<ImageView>(R.id.imageView)
        val saveButton = v.findViewById<Button>(R.id.save)
        imageView.transitionName = transitionName
        Glide.with(this)
            .load(imageSource.fullSizeImage)
            .into(imageView)
        saveButton.setOnClickListener { copyCache() }
        return v
    }

    private fun copyCache() {
        if (ioThread == null || !ioThread!!.isAlive) {
            ioThread = Thread() {
                val file: File = Glide.with(this)
                    .asFile()
                    .load(imageSource.fullSizeImage)
                    .submit()
                    .get()
                val calendar=Calendar.getInstance()
                val date="${calendar.get(Calendar.YEAR)}-${calendar.get(Calendar.MONTH)}" +
                        "-${calendar.get(Calendar.DAY_OF_MONTH)}"
                val newName="${date}-${file.name.substring(0..5)}.jpg"
                val newFile = File(
                    Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                    newName
                )
                if (!newFile.exists()) {
                    file.copyTo(newFile)
                    this@DetailFragment.activity?.runOnUiThread {
                        MediaScannerConnection.scanFile(
                            activity
                            , arrayOf(newFile.path)
                            , arrayOf("image/jpeg")
                            , null
                        )
                        toast("已保存到 ${newFile.path}")
                    }
                }else{
                    this@DetailFragment.activity?.runOnUiThread {
                        toast("已经存过了，路径 ${newFile.path}")
                    }
                }
            }
            ioThread!!.start()
        }
    }

}