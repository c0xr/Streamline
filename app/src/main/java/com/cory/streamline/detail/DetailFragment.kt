package com.cory.streamline.detail

import android.os.Bundle
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
import com.cory.streamline.model.ImageSource
import com.cory.streamline.util.log
import com.cory.streamline.util.toast


private const val ARG_TRANSITION_NAME = "transitionName"
private const val ARG_IMAGE_SOURCE = "imageSource"

class DetailFragment : Fragment(), IDetailView {
    private lateinit var detailPresenter: IDetailPresenter
    private lateinit var transitionName: String
    private lateinit var imageSource: ImageSource
    private var preferLayoutId: Int = 0
    private var isFavorite = false
    private lateinit var favoriteButton: Button

    companion object {
        val FRAGMENT_DETAIL_TAG = "fragment detail tag"
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
        detailPresenter = DetailPresenter(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        detailPresenter.requirePreferLayoutId(true)
        val v = inflater.inflate(preferLayoutId, container, false)
        val imageView = v.findViewById<ImageView>(R.id.imageView)
        val saveButton = v.findViewById<Button>(R.id.save)
        favoriteButton = v.findViewById<Button>(R.id.favorite)
        val backButton = v.findViewById<ImageButton>(R.id.back)
        val categoryTextView = v.findViewById<TextView>(R.id.category)
        val resolutionTextView = v.findViewById<TextView>(R.id.resolution)
        val fileSizeTextView = v.findViewById<TextView>(R.id.fileSize)

        imageView.transitionName = transitionName
        Glide.with(this)
            .load(imageSource.fullSizeImage)
            .transition(GenericTransitionOptions.with(R.anim.fade_in))
            .into(imageView)
        saveButton.setOnClickListener {
            detailPresenter.copyCache(imageSource.fullSizeImage)
        }
        detailPresenter.requireFavoriteState(imageSource.thumbnailImage)
        favoriteButton.setOnClickListener {
            favoriteButton.isClickable = false
            if (!isFavorite) {
                detailPresenter.saveToFavorite(imageSource)
            } else {
                detailPresenter.deleteFromFavorite(imageSource.thumbnailImage)
            }
        }
        backButton.setOnClickListener {
            val transaction = activity!!.supportFragmentManager.beginTransaction()
            activity!!.supportFragmentManager.popBackStack()
            transaction.remove(this)
            transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE)
            transaction.commit()
        }
        imageView.setOnClickListener {
            startActivity(
                SubsampleActivity.newIntent(activity!!, imageSource.fullSizeImage)
            )
        }
        categoryTextView.text = "分类：${imageSource.category}"
        resolutionTextView.text = "分辨率：${imageSource.resolution}"
        val sizeMb = imageSource.fileSize / 1024 / 1024.0f
        val sizeS = String.format("%.2f", sizeMb)
        fileSizeTextView.text = "大小：$sizeS MB"
        return v
    }

    override fun onResume() {
        super.onResume()
        detailPresenter.requirePreferLayoutId(false)
    }

    override fun onCacheCopied(filePath: String, duplicated: Boolean) {
        if (duplicated) {
            toast("已经存过了，路径 $filePath")
        } else {
            toast("已保存到 $filePath")
        }
    }

    override fun onFavoriteSaved(success: Boolean) {
        if (success) {
            log("收藏图片：成功")
            isFavorite = true
            favoriteButton.text = "取消收藏"
        } else {
            log("收藏图片：失败")
            toast("发生了一些错误，收藏失败")
        }
        favoriteButton.isClickable = true
    }

    override fun onFavoriteDelete(success: Boolean) {
        if (success) {
            log("取消收藏：成功")
            isFavorite = false
            favoriteButton.text = "收藏"
        } else {
            log("取消图片：失败")
            toast("发生了一些错误，取消收藏失败")
        }
        favoriteButton.isClickable = true
    }

    override fun onFavoriteStateReceived(isFavorite: Boolean) {
        if (isFavorite) {
            log("获取收藏状态：成功，已收藏")
            this.isFavorite = true
            favoriteButton.text = "取消收藏"

        } else {
            log("获取收藏状态：成功，未收藏")
            this.isFavorite = false
            favoriteButton.text = "收藏"
        }
        favoriteButton.isClickable = true
    }

    override fun onPreferLayoutIdReceived(preferLayoutId: Int, isInitial: Boolean) {
        if (isInitial) {
            this.preferLayoutId = preferLayoutId
        } else {
            val fragment: Fragment? =
                activity!!.supportFragmentManager.findFragmentByTag(FRAGMENT_DETAIL_TAG)
            log(
                "重建详情碎片，碎片是否存在：${fragment != null} " +
                        "布局id是否相同：${preferLayoutId == this.preferLayoutId}"
            )
            if (fragment != null && preferLayoutId != this.preferLayoutId) {
                activity!!.supportFragmentManager.beginTransaction()
                    .detach(fragment)
                    .attach(fragment)
                    .commit()
            }
        }
    }

}