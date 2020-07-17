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
import com.cory.streamline.model.SOURCE_FREEPIK
import xander.elasticity.ElasticityHelper
import java.util.ArrayList


class MainFragment : Fragment() {

    private var iconList=ArrayList<Map<String,Int>>()
    @SuppressLint("ResourceType")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        val v=inflater.inflate(R.layout.fragment_main,container,false)
        initData()
        val from: Array<String> = arrayOf(KEY_IMG)
        val to:IntArray= intArrayOf(R.id.img)
        val adapter=SimpleAdapter(context,iconList,R.layout.web_item,from,to)
        val gridView=v.findViewById<GridView>(R.id.grid_view)
        val galleryFragment=GalleryFragment.newIntent(SOURCE_FREEPIK)
        gridView.adapter=adapter
        gridView.onItemClickListener=
            AdapterView.OnItemClickListener { _, _, position, _ ->
                when(position){
                    1->activity!!.supportFragmentManager.beginTransaction()
                        .setCustomAnimations(R.animator.anim_fragment_start, android.R.animator.fade_out)
                        .add(R.id.fragment_container,
                            galleryFragment,
                            MainActivity.FRAGMENT_GALLERY_TAG
                        ).addToBackStack(null)
                        .commit()
                }
            }


        return v
    }

    private fun initData(){
        val icon:IntArray= intArrayOf(R.drawable.ic_fragment_main_wallhaven,R.drawable.ic_fragment_main_freepik)
        for (i in icon){
            var map= hashMapOf<String,Int>()
            map[KEY_IMG] = i
            iconList.add(map)
        }
    }
    companion object{
        const val KEY_IMG="img"
    }
}