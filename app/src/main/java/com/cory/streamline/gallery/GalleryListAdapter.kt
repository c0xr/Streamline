package com.cory.streamline.gallery

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.GenericTransitionOptions
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.cory.streamline.R
import com.cory.streamline.home.HomeActivity
import com.cory.streamline.model.remote.RemoteResponse
import com.cory.streamline.model.remote.RemoteSource
import com.cory.streamline.model.web.ImageSource
import com.cory.streamline.model.web.ImageWrapper
import com.cory.streamline.util.log
import com.cory.streamline.util.toast
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observer
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers

private const val VIEW_TYPE_COMMON = 0
private const val VIEW_TYPE_FOOTER = 1

open class GalleryListAdapter(
    var imageSources: MutableList<ImageSource>,
    private val context: Context
) :
    RecyclerView.Adapter<GalleryListAdapter.BaseHolder>() {

    open class BaseHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    }

    open class ThumbnailHolder(
        itemView: View
    ) : BaseHolder(itemView), View.OnClickListener {
        lateinit var imageSource: ImageSource
        val imageView: ImageView = itemView.findViewById(R.id.imageView)

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            imageView.transitionName = "image$adapterPosition"
            recordHistory(imageSource)
            val activity = itemView.context as AppCompatActivity
            val fragment = activity.supportFragmentManager
                .findFragmentByTag(HomeActivity.FRAGMENT_GALLERY_TAG) as GalleryFragment
            fragment.startDetailFragment(imageView.transitionName, imageSource)
        }

        private fun recordHistory(imageSource: ImageSource) {
            //TODO replace token use login util
            val imageWrapper = ImageWrapper("token", imageSource)
            RemoteSource.saveToHistory(imageWrapper)
                .observeOn(Schedulers.io())
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<RemoteResponse> {
                    override fun onComplete() {
                    }

                    override fun onSubscribe(d: Disposable) {
                    }

                    override fun onNext(t: RemoteResponse) {
                        log("onNext,element:$t")
                    }

                    override fun onError(e: Throwable) {
                        log("onError:$e")
                        toast("onError:$e")
                    }

                })
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == imageSources.size) {
            VIEW_TYPE_FOOTER
        } else {
            VIEW_TYPE_COMMON
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseHolder {
        val inflater = LayoutInflater.from(parent.context)
        val v: View
        return if (viewType == VIEW_TYPE_COMMON) {
            v = inflater.inflate(R.layout.item_view_thumbnail, parent, false)
            ThumbnailHolder(v)
        } else {
            v = inflater.inflate(R.layout.item_view_footer, parent, false)
            BaseHolder(v)
        }
    }

    override fun getItemCount(): Int {
        return if (imageSources.size == 0) 0
        else imageSources.size + 1
    }

    override fun onBindViewHolder(holder: BaseHolder, position: Int) {
        if (holder !is ThumbnailHolder) return

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

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        val manager = recyclerView.layoutManager as GridLayoutManager
        manager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                return if (getItemViewType(position) == VIEW_TYPE_FOOTER) manager.spanCount
                else 1
            }
        }
    }
}