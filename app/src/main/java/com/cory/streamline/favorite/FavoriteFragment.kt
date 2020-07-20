package com.cory.streamline.favorite

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import com.cory.streamline.R
import com.cory.streamline.gallery.GalleryFragment
import com.cory.streamline.gallery.GalleryListAdapter

class FavoriteFragment : GalleryFragment() {
    companion object {
        val ARG_WEB_NAME = "webName"
        fun newInstance(webName: String): FavoriteFragment {
            val bundle = Bundle()
            bundle.putSerializable(ARG_WEB_NAME, webName)
            val fragment = FavoriteFragment()
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val v = inflater.inflate(R.layout.fragment_gallery, container, false)
        recyclerView = v.findViewById(R.id.recyclerView)
        recyclerView.layoutManager = GridLayoutManager(activity, 2)
        val adapter = recyclerView.adapter as FavoriteListAdapter?
        if (adapter == null) {
            favoriteListAdapter = FavoriteListAdapter(mutableListOf(), activity!!)
            recyclerView.adapter = favoriteListAdapter
            galleryPresenter.getPopularResults(true)
        } else {
            favoriteListAdapter = adapter
        }

        mSpinKit = v.findViewById(R.id.spinKit)
        mSpinKit.visibility = View.VISIBLE
        return v
    }
}