package com.cory.streamline.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import com.cory.streamline.R

class MainFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val v=inflater.inflate(R.layout.fragment_main,container,false)
        val button: Button =v.findViewById(R.id.button)
        button.setOnClickListener {
            activity!!.supportFragmentManager.beginTransaction()
                .setCustomAnimations(R.animator.anim_fragment_start, android.R.animator.fade_out)
                .replace(R.id.fragment_container,
                    SecondFragment()
                )
                .commit()
        }
        return v
    }
}