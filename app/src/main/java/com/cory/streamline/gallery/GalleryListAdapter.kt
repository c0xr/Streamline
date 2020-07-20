package com.cory.streamline.gallery

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.GenericTransitionOptions
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.cory.streamline.R
import com.cory.streamline.home.HomeActivity
import com.cory.streamline.model.web.ImageSource


open class GalleryListAdapter(var imageSources: MutableList<ImageSource>, private val context: Context) :
    RecyclerView.Adapter<GalleryListAdapter.ThumbnailHolder>() {

    open class ThumbnailHolder(
        itemView: View
    ) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        lateinit var imageSource: ImageSource
        val imageView: ImageView = itemView.findViewById(R.id.imageView)

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            imageView.transitionName="image$adapterPosition"
            val activity=itemView.context as AppCompatActivity
            val fragment=activity.supportFragmentManager
                .findFragmentByTag(HomeActivity.FRAGMENT_GALLERY_TAG) as GalleryFragment
            fragment.startDetailFragment(imageView.transitionName,imageSource)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ThumbnailHolder {
        val inflater = LayoutInflater.from(parent.context)
        val v = inflater.inflate(R.layout.item_view_thumbnail, parent, false)
        return ThumbnailHolder(v)
    }

    override fun getItemCount(): Int {
        return imageSources.size
    }

    override fun onBindViewHolder(holder: ThumbnailHolder, position: Int) {
        val source = imageSources[position]
        val imageView = holder.imageView

        holder.imageSource = source
        Glide.with(context)
            .clear(imageView)
        Glide.with(context)
            .load(source.thumbnailImage)
            .transition(GenericTransitionOptions.with(R.anim.fade_in_and_fill))
            .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
            .into(imageView)
    }
}