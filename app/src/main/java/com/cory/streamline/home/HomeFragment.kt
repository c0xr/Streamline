package com.cory.streamline.home

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.GridView
import android.widget.SimpleAdapter

import androidx.fragment.app.Fragment
import com.cory.streamline.R
import com.cory.streamline.gallery.GalleryFragment
import com.cory.streamline.util.SOURCE_UNSPLASH
import com.cory.streamline.util.SOURCE_WALLHAVEN
import com.cory.streamline.util.toast
import java.lang.IllegalStateException
import java.util.ArrayList


class HomeFragment : Fragment() {

    private var iconList = ArrayList<Map<String, Int>>()

    @SuppressLint("ResourceType")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val v = inflater.inflate(R.layout.fragment_main, container, false)
        initData()
        val from: Array<String> = arrayOf(KEY_IMG)
        val to: IntArray = intArrayOf(R.id.img)
        val adapter = SimpleAdapter(context, iconList, R.layout.web_item, from, to)
        val gridView = v.findViewById<GridView>(R.id.grid_view)
        gridView.adapter = adapter
        gridView.onItemClickListener =
            AdapterView.OnItemClickListener { _, _, position, _ ->
                val sourceString = when (position) {
                    0 -> SOURCE_WALLHAVEN
                    1 -> SOURCE_UNSPLASH
                    else -> throw IllegalStateException("GirdView index $position out of bounds")
                }
                if(sourceString== SOURCE_UNSPLASH) {
                    toast("站点对接中，暂时不能使用")
                    return@OnItemClickListener
                }
                val galleryFragment = GalleryFragment.newInstance(sourceString)
                activity!!.supportFragmentManager.beginTransaction()
                    .setCustomAnimations(
                        R.animator.anim_fragment_start,
                        0,
                        0,
                        R.animator.anim_fragment_end
                    )
                    .add(
                        R.id.fragment_container,
                        galleryFragment,
                        HomeActivity.FRAGMENT_GALLERY_TAG
                    )
                    .addToBackStack(HomeActivity.FRAGMENT_GALLERY_TAG)
                    .commit()
            }
        return v
    }

    private fun initData() {
        val icon: IntArray =
            intArrayOf(R.drawable.pic_wallhaven_logo, R.drawable.pic_unsplash_logo)
        for (i in icon) {
            var map = hashMapOf<String, Int>()
            map[KEY_IMG] = i
            iconList.add(map)
        }
    }

    companion object {
        const val KEY_IMG = "img"
    }
}