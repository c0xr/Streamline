package com.cory.streamline.history

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.cory.streamline.R
import com.cory.streamline.gallery.GalleryListAdapter
import com.cory.streamline.model.web.ImageSource

class HistoryListAdapter(imageSources: MutableList<ImageSource>, context: Context) :
    GalleryListAdapter(imageSources, context) {

    class HistoryImageHolder(itemView: View) : ThumbnailHolder(itemView) {
        var textView: TextView = itemView.findViewById(R.id.time)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseHolder {
        val inflater = LayoutInflater.from(parent.context)
        val v: View
        return if (viewType == VIEW_TYPE_COMMON) {
            v = inflater.inflate(R.layout.item_view_history, parent, false)
            HistoryImageHolder(v)
        } else {
            v = inflater.inflate(R.layout.item_view_footer, parent, false)
            BaseHolder(v)
        }
    }

    override fun onBindViewHolder(holder: BaseHolder, position: Int) {
        super.onBindViewHolder(holder, position)
        if (holder !is ThumbnailHolder) return

        val source = imageSources[position]
        val historyImageHolder = holder as HistoryImageHolder
        historyImageHolder.textView.text = source.sourceCreatedTime
    }
}