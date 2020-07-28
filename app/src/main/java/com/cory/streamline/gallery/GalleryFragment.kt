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
import com.cory.streamline.model.ImageSource
import com.cory.streamline.util.CATEGORY_POPULAR
import com.cory.streamline.util.log
import com.github.ybq.android.spinkit.SpinKitView
import kotlin.IllegalArgumentException


open class GalleryFragment : Fragment(), IGalleryView {
    protected lateinit var galleryPresenter: IGalleryPresenter
    protected lateinit var galleryListAdapter: GalleryListAdapter
    protected lateinit var recyclerView: RecyclerView
    protected lateinit var mSpinKit: SpinKitView
    private var lastFetchingSize = 0
    private var listener = object : RecyclerView.OnScrollListener() {
        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            if (!recyclerView.canScrollVertically(1)) {
                recyclerView.removeOnScrollListener(this)
                galleryListAdapter.info = GalleryListAdapter.INFO_LOADING
                galleryListAdapter.notifyItemChanged(galleryListAdapter.imageSources.size)
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
            galleryListAdapter = GalleryListAdapter(mutableListOf(), activity!!)
            recyclerView.adapter = galleryListAdapter
            galleryPresenter.fetchingImages(true)
        } else {
            galleryListAdapter = adapter
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
        val list = imageSources.toMutableList()
        galleryListAdapter.imageSources = list
        lastFetchingSize = list.size
        galleryListAdapter.notifyDataSetChanged()
        recyclerView.addOnScrollListener(listener)
        mSpinKit.visibility = View.GONE
    }

    override fun onMoreImagesFetched(imageSources: List<ImageSource>) {
        galleryListAdapter.imageSources.addAll(imageSources)
        if (lastFetchingSize > imageSources.size) {
            galleryListAdapter.info = GalleryListAdapter.INFO_LOADED
            recyclerView.removeOnScrollListener(listener)
        } else {
            galleryListAdapter.info = GalleryListAdapter.INFO_LOADABLE
            lastFetchingSize = imageSources.size
            recyclerView.addOnScrollListener(listener)
        }
        lastFetchingSize=imageSources.size
        galleryListAdapter.notifyDataSetChanged()
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