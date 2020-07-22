package com.cory.streamline.history

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import com.cory.streamline.R
import com.cory.streamline.gallery.GalleryFragment

class HistoryFragment : GalleryFragment() {
    companion object {
        val ARG_WEB_NAME = "webName"
        fun newInstance(webName: String): HistoryFragment {
            val bundle = Bundle()
            bundle.putSerializable(ARG_WEB_NAME, webName)
            val fragment = HistoryFragment()
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
        val adapter = recyclerView.adapter as HistoryListAdapter?
        if (adapter == null) {
            favoriteListAdapter =
                HistoryListAdapter(
                    mutableListOf(),
                    activity!!
                )
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