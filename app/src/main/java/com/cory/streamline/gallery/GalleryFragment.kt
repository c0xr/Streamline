package com.cory.streamline.gallery

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cory.streamline.R
import com.cory.streamline.detail.DetailFragment
import com.cory.streamline.model.web.ImageSource
import com.cory.streamline.util.CATEGORY_POPULAR
import com.cory.streamline.util.SOURCE_WALLHAVEN
import com.cory.streamline.util.log
import com.cory.streamline.util.toast
import com.github.ybq.android.spinkit.SpinKitView
import kotlin.IllegalArgumentException


open class GalleryFragment : Fragment(), IGalleryView {
    protected lateinit var galleryPresenter: IGalleryPresenter
    protected lateinit var favoriteListAdapter: GalleryListAdapter
    protected lateinit var recyclerView: RecyclerView
    protected lateinit var mSpinKit: SpinKitView
    private var listener = object : RecyclerView.OnScrollListener() {
        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            if (!recyclerView.canScrollVertically(1)) {
                recyclerView.removeOnScrollListener(this)
                galleryPresenter.fetchingImages(false)
                log("fetching more images..")
            }
        }
    }

    companion object {
        private const val ARG_SOURCE_STRING = "sourceString"
        private const val ARG_CATEGORY = "category"
        fun newInstance(
            sourceString: String,
            category: String = CATEGORY_POPULAR
        ): GalleryFragment {
            val bundle = Bundle()
            bundle.putString(ARG_SOURCE_STRING, sourceString)
            bundle.putString(ARG_CATEGORY, category)
            val fragment = GalleryFragment()
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val sourceString = arguments?.getString(ARG_SOURCE_STRING, "")
            ?: throw IllegalArgumentException("argument is null")
        val category = arguments?.getString(ARG_CATEGORY, "")
            ?: throw IllegalArgumentException("argument is null")
        if (sourceString == "" || category == "") {
            throw IllegalArgumentException("sourceString or category is null")
        }
        galleryPresenter = GalleryPresenter(
            this,
            sourceString,
            category
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val v = inflater.inflate(R.layout.fragment_gallery, container, false)
        recyclerView = v.findViewById(R.id.recyclerView)
        recyclerView.layoutManager = GridLayoutManager(activity, 2)
        val adapter = recyclerView.adapter as GalleryListAdapter?
        if (adapter == null) {
            favoriteListAdapter = GalleryListAdapter(mutableListOf(), activity!!)
            recyclerView.adapter = favoriteListAdapter
            galleryPresenter.fetchingImages(true)
        } else {
            favoriteListAdapter = adapter
        }
        mSpinKit = v.findViewById(R.id.spinKit)
        mSpinKit.visibility = View.VISIBLE
        return v
    }

    override fun onDestroy() {
        galleryPresenter.onDestroy()
        super.onDestroy()
    }

    override fun onImagesFetched(imageSources: List<ImageSource>) {
        favoriteListAdapter.imageSources = imageSources.toMutableList()
        favoriteListAdapter.notifyDataSetChanged()
        recyclerView.addOnScrollListener(listener)
        mSpinKit.visibility = View.GONE
    }

    override fun onMoreImagesFetched(imageSources: List<ImageSource>) {
        favoriteListAdapter.imageSources.addAll(imageSources)
        favoriteListAdapter.notifyDataSetChanged()
        recyclerView.addOnScrollListener(listener)
    }

    fun startDetailFragment(transitionName: String, imageSource: ImageSource) {
        val fragment = DetailFragment.newInstance(transitionName, imageSource)
        activity!!.supportFragmentManager.beginTransaction()
            .setCustomAnimations(
                R.animator.anim_fragment_start,
                0,
                0,
                R.animator.anim_fragment_end
            )
            .add(R.id.fragment_container, fragment, DetailFragment.FRAGMENT_DETAIL_TAG)
            .addToBackStack(DetailFragment.FRAGMENT_DETAIL_TAG)
            .commit()
    }

}