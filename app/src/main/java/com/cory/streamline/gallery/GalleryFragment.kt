package com.cory.streamline.gallery

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cory.streamline.R
import com.cory.streamline.detail.DetailFragment
import com.cory.streamline.model.CATEGORY_POPULAR
import com.cory.streamline.model.SOURCE_FREEPIK
import com.cory.streamline.util.log
import com.cory.streamline.util.toast


class GalleryFragment : Fragment(), IGalleryView {
    private lateinit var galleryPresenter: IGalleryPresenter

    private lateinit var galleryListAdapter: GalleryListAdapter
    private lateinit var recyclerView: RecyclerView
    private  var listener = object : RecyclerView.OnScrollListener() {
        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            if (!recyclerView.canScrollVertically(1)) {
                recyclerView.removeOnScrollListener(this)
                galleryPresenter.fetchMoreThumbnails()
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
        Toast.makeText(context,arguments?.getSerializable(ARG_WEB_NAME).toString(),Toast.LENGTH_SHORT).show()
        val v = inflater.inflate(R.layout.fragment_gallery, container, false)
        recyclerView = v.findViewById(R.id.recyclerView)
        recyclerView.layoutManager = GridLayoutManager(activity, 2)

        val adapter = recyclerView.adapter as GalleryListAdapter?
        if (adapter == null) {
            galleryListAdapter = GalleryListAdapter(mutableListOf(), activity!!)
            recyclerView.adapter = galleryListAdapter
            galleryPresenter.fetchThumbnails()
        } else {
            galleryListAdapter = adapter
        }

        return v
    }

    override fun onDestroy() {
        galleryPresenter.clearReference()
        super.onDestroy()
    }

    override fun onFetchingCompleted(thumbnails: List<String>) {
        galleryListAdapter.thumbnailUrls = thumbnails.toMutableList()
        galleryListAdapter.notifyDataSetChanged()
        recyclerView.addOnScrollListener(listener)
    }

    override fun onFetchingMoreCompleted(thumbnails: List<String>) {
        val itemsCount = galleryListAdapter.thumbnailUrls.size-1
        galleryListAdapter.thumbnailUrls.addAll(thumbnails)
        galleryListAdapter.notifyDataSetChanged()
        recyclerView.post {
            recyclerView.smoothScrollToPosition(itemsCount+6)
        }
        recyclerView.addOnScrollListener(listener)
    }

    fun startDetailFragment(transitionName:String,thumbnailUrl:String){
        val fragment=DetailFragment.newInstance(transitionName,thumbnailUrl)
        activity!!.supportFragmentManager.beginTransaction()
            .setCustomAnimations(R.animator.anim_fragment_start, android.R.animator.fade_out)
            .add(R.id.fragment_container,fragment)
            .addToBackStack("Transition")
            .commit()
    }

    companion object{
        val ARG_WEB_NAME="webName"
        fun newIntent(webName:String):GalleryFragment{
            val bundle=Bundle()
            bundle.putSerializable(ARG_WEB_NAME,webName)
            val fragment=GalleryFragment()
            fragment.arguments=bundle
            return fragment
        }
    }
}