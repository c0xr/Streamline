package com.cory.streamline.history

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import com.cory.streamline.R
import com.cory.streamline.gallery.GalleryFragment
import com.cory.streamline.util.CATEGORY_POPULAR

class HistoryFragment : GalleryFragment() {
    companion object {
        private const val ARG_SOURCE_STRING = "sourceString"
        private const val ARG_CATEGORY = "category"
        fun newInstance(
            sourceString: String,
            category: String = CATEGORY_POPULAR
        ): HistoryFragment {
            val bundle = Bundle()
            bundle.putString(ARG_SOURCE_STRING, sourceString)
            bundle.putString(ARG_CATEGORY, category)
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
            galleryPresenter.fetchingImages(true)
        } else {
            favoriteListAdapter = adapter
        }

        mSpinKit = v.findViewById(R.id.spinKit)
        mSpinKit.visibility = View.VISIBLE
        return v
    }
}