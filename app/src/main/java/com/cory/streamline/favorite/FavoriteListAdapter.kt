package com.cory.streamline.favorite

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.cory.streamline.R
import com.cory.streamline.gallery.GalleryListAdapter
import com.cory.streamline.model.web.ImageSource

class FavoriteListAdapter(imageSources: MutableList<ImageSource>, context: Context) :
    GalleryListAdapter(imageSources, context) {

    class FavoriteImageHolder(itemView: View) : ThumbnailHolder(itemView) {
        var textView: TextView = itemView.findViewById(R.id.time)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ThumbnailHolder {
        val inflater = LayoutInflater.from(parent.context)
        val v = inflater.inflate(R.layout.item_view_favorite, parent, false)
        return FavoriteImageHolder(v)
    }

    override fun onBindViewHolder(holder: ThumbnailHolder, position: Int) {
        super.onBindViewHolder(holder, position)
        val source = imageSources[position]
        val favoriteImageHolder = holder as FavoriteImageHolder
        favoriteImageHolder.textView.text = source.sourceCreatedTime
    }
}