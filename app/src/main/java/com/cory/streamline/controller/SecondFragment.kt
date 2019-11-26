package com.cory.streamline.controller

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import com.cory.streamline.R
import com.cory.streamline.model.ImageUrlFetcher
import com.cory.streamline.util.getBaseUrl

class SecondFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val v=inflater.inflate(R.layout.fragment_second,container,false)

        val button: Button =v.findViewById(R.id.button)
        val button2 = v.findViewById<Button>(R.id.button2)
        button.setOnClickListener {
            activity!!.supportFragmentManager.beginTransaction()
                .setCustomAnimations(R.animator.anim_fragment_start, android.R.animator.fade_out)
                .replace(R.id.fragment_container, MainFragment())
                .commit()
        }

        button2.setOnClickListener {
            startActivity(Intent(activity,SecondActivity::class.java))
        }

        val handler=object : Handler() {
            override fun handleMessage(msg: Message?) {
                val array=msg?.data?.getSerializable("urls") as Array<String>
                button.text=array[0]
            }
        }

        val fetcher=ImageUrlFetcher(getBaseUrl())
        fetcher.setListener{
            if(it!=null) {
                val msg = Message()
                val bundle = Bundle()
                bundle.putSerializable("urls", it.toTypedArray())
                msg.data = bundle
                handler.handleMessage(msg)
            }
        }
        fetcher.execute()
        return v
    }
}