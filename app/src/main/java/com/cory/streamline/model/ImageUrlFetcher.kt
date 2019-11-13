package com.cory.streamline.model

import android.os.AsyncTask
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import java.io.IOException

class ImageUrlFetcher(private val url: String) : AsyncTask<Unit, Unit, Unit>() {
    private var mImageUrls: List<String>?=null
    private var mTaskListener:TaskListener?=null

    interface TaskListener{
        fun onFinish(imageUrls:List<String>?)
    }

    fun setListener(action: (List<String>?)->Unit){
        mTaskListener=object :TaskListener{
            override fun onFinish(imageUrls: List<String>?) {
                action(imageUrls)
            }
        }
    }

    override fun doInBackground(vararg params: Unit?) {
        var doc: Document? = null
        try {
            doc = Jsoup.connect(url).get()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        if(doc!=null) {
            val elements = doc.select("img[src~=(?i)\\.(png|jpe?g)]")
            mImageUrls= List(elements.size) {elements[it].attr("src")}
        }
        mTaskListener?.onFinish(mImageUrls)
        return
    }
}