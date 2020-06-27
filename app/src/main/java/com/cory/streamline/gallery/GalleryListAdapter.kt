package com.cory.streamline.gallery

import android.animation.Animator
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.GenericTransitionOptions
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.cory.streamline.R
import com.cory.streamline.util.toast

class GalleryListAdapter(var thumbnailUrls: MutableList<String>, private val context: Context) :
    RecyclerView.Adapter<GalleryListAdapter.ThumbnailHolder>() {

    class ThumbnailHolder(
        itemView: View
    ) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        lateinit var thumbnailUrl: String
        val imageView: ImageView = itemView.findViewById(R.id.imageView)

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            toast("click image!")
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ThumbnailHolder {
        val inflater = LayoutInflater.from(parent.context)
        val v = inflater.inflate(R.layout.item_view_thumbnail, parent, false)
        return ThumbnailHolder(v)
    }

    override fun getItemCount(): Int {
        return thumbnailUrls.size
    }

    override fun onBindViewHolder(holder: ThumbnailHolder, position: Int) {
        val url = thumbnailUrls[position]
        val imageView = holder.imageView

        holder.thumbnailUrl = url
        Glide.with(context)
            .clear(imageView)
        Glide.with(context)
            .load(url)
            .transition(GenericTransitionOptions.with(R.anim.fade_in_and_fill))
            .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
            .into(imageView)
    }
}