package com.cory.streamline.detail

import android.media.MediaScannerConnection
import android.os.Bundle
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.bumptech.glide.GenericTransitionOptions
import com.bumptech.glide.Glide
import com.cory.streamline.R
import com.cory.streamline.model.remote.RemoteService
import com.cory.streamline.model.web.ImageSource
import com.cory.streamline.retrofit.ServiceGenerator
import com.cory.streamline.util.toast
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import java.io.File
import java.util.*


private const val ARG_TRANSITION_NAME = "transitionName"
private const val ARG_IMAGE_SOURCE = "imageSource"

class DetailFragment : Fragment() {
    private lateinit var transitionName: String
    private lateinit var imageSource: ImageSource
    private lateinit var ioThread: Thread

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
        val favoriteButton = v.findViewById<Button>(R.id.favorite)
        val backButton = v.findViewById<ImageButton>(R.id.back)
        val categoryTextView = v.findViewById<TextView>(R.id.category)
        val resolutionTextView = v.findViewById<TextView>(R.id.resolution)
        val fileSizeTextView = v.findViewById<TextView>(R.id.fileSize)

        imageView.transitionName = transitionName
        Glide.with(this)
            .load(imageSource.fullSizeImage)
            .transition(GenericTransitionOptions.with(R.anim.fade_in))
            .into(imageView)
        saveButton.setOnClickListener { copyCache() }
        favoriteButton.setOnClickListener { saveToFavorite() }
        backButton.setOnClickListener {
            val transaction = activity!!.supportFragmentManager.beginTransaction()
            activity!!.supportFragmentManager.popBackStack()
            transaction.remove(this)
            transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE)
            transaction.commit()
        }
        imageView.setOnClickListener {
            startActivity(
                SubsampleActivity.newIntent(activity!!,imageSource.fullSizeImage)
            )
        }
        categoryTextView.text = "分类：${imageSource.category}"
        resolutionTextView.text = "分辨率：${imageSource.resolution}"
        val sizeMb = imageSource.fileSize / 1024 / 1024.0f
        val sizeS = String.format("%.2f", sizeMb)
        fileSizeTextView.text = "大小：$sizeS MB"

        return v
    }

    private fun copyCache() {
        if (!this::ioThread.isInitialized || !ioThread.isAlive) {
            ioThread = Thread() {
                val file: File = Glide.with(this)
                    .asFile()
                    .load(imageSource.fullSizeImage)
                    .submit()
                    .get()
                val calendar = Calendar.getInstance()
                val date = "${calendar.get(Calendar.YEAR)}-${calendar.get(Calendar.MONTH)}" +
                        "-${calendar.get(Calendar.DAY_OF_MONTH)}"
                val newName = "${date}-${file.name.substring(0..5)}.jpg"
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
                } else {
                    this@DetailFragment.activity?.runOnUiThread {
                        toast("已经存过了，路径 ${newFile.path}")
                    }
                }
            }
            ioThread.start()
        }
    }

    private fun saveToFavorite() {
        ServiceGenerator.createRemoteService(RemoteService::class.java)
            .saveToFavorite()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                if (it.success) {
                    toast("收藏完成")
                } else {
                    toast("发生了一些错误，收藏失败")
                }
            }
    }

}