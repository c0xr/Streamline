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


class GalleryFragment : Fragment(), IGalleryView {
    private lateinit var galleryPresenter: IGalleryPresenter
    private lateinit var galleryListAdapter: GalleryListAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var mSpinKit: SpinKitView
    private var listener = object : RecyclerView.OnScrollListener() {
        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            if (!recyclerView.canScrollVertically(1)) {
                recyclerView.removeOnScrollListener(this)
                galleryPresenter.getPopularResults(false)
                toast("fetching more image..")
                log("fetching more image..")
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
            galleryListAdapter = GalleryListAdapter(mutableListOf(), activity!!)
            recyclerView.adapter = galleryListAdapter
            galleryPresenter.getPopularResults(true)
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
        galleryListAdapter.imageSources = imageSources.toMutableList()
        galleryListAdapter.notifyDataSetChanged()
        recyclerView.addOnScrollListener(listener)
        mSpinKit.visibility = View.GONE
    }

    override fun onMoreImagesFetched(imageSources: List<ImageSource>) {
        val itemsCount = galleryListAdapter.imageSources.size - 1
        galleryListAdapter.imageSources.addAll(imageSources)
        galleryListAdapter.notifyDataSetChanged()
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
            .add(R.id.fragment_container, fragment, "DetailFragment")
            .addToBackStack("DetailFragment")
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