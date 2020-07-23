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
import com.cory.streamline.util.log
import com.cory.streamline.util.toast
import com.github.ybq.android.spinkit.SpinKitView


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
                galleryPresenter.getPopularResults(false)
                toast("fetching more images..")
                log("fetching more images..")
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        galleryPresenter = GalleryPresenter(
            this,
            arguments?.getSerializable(ARG_WEB_NAME).toString(),
            CATEGORY_POPULAR
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
            galleryPresenter.getPopularResults(true)
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
        val itemsCount = favoriteListAdapter.imageSources.size - 1
        favoriteListAdapter.imageSources.addAll(imageSources)
        favoriteListAdapter.notifyDataSetChanged()
        recyclerView.post {
            recyclerView.smoothScrollToPosition(itemsCount + 6)
        }
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

    companion object {
        val ARG_WEB_NAME = "webName"
        fun newInstance(webName: String): GalleryFragment {
            val bundle = Bundle()
            bundle.putSerializable(ARG_WEB_NAME, webName)
            val fragment = GalleryFragment()
            fragment.arguments = bundle
            return fragment
        }
    }
}