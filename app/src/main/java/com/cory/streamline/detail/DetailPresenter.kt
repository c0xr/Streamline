package com.cory.streamline.detail

import android.content.Context
import android.media.MediaScannerConnection
import android.os.Environment
import com.bumptech.glide.Glide
import com.cory.streamline.R
import com.cory.streamline.model.remote.RemoteService
import com.cory.streamline.model.web.ImageSource
import com.cory.streamline.model.web.ImageWrapper
import com.cory.streamline.retrofit.ServiceGenerator
import com.cory.streamline.setting.LayoutCustomActivity
import com.cory.streamline.util.globalContext
import com.cory.streamline.util.toast
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import java.io.File
import java.util.*

class DetailPresenter(
    private var detailView: IDetailView?
) : IDetailPresenter {
    private lateinit var ioThread: Thread

    override fun copyCache(fullSizeUrl: String) {
        if (!this::ioThread.isInitialized || !ioThread.isAlive) {
            ioThread = Thread() {
                val file: File = Glide.with(detailView as DetailFragment)
                    .asFile()
                    .load(fullSizeUrl)
                    .submit()
                    .get()
                val calendar = Calendar.getInstance()
                val date = "${calendar.get(Calendar.YEAR)}-${calendar.get(Calendar.MONTH)}" +
                        "-${calendar.get(Calendar.DAY_OF_MONTH)}"
                val newName = "${date}-${file.name.substring(0..5)}.jpg"
                val newFile = File(
                    Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                    newName
                )
                if (!newFile.exists()) {
                    file.copyTo(newFile)
                    (detailView as DetailFragment).activity?.runOnUiThread {
                        MediaScannerConnection.scanFile(
                            (detailView as DetailFragment).activity
                            , arrayOf(newFile.path)
                            , arrayOf("image/jpeg")
                            , null
                        )
                        detailView?.onCacheCopied(newFile.path, false)
                    }
                } else {
                    (detailView as DetailFragment).activity?.runOnUiThread {
                        detailView?.onCacheCopied(newFile.path, true)
                    }
                }
            }
            ioThread.start()
        }
    }

    override fun saveToFavorite(imageSource: ImageSource) {
        val wrapper = ImageWrapper("token", imageSource)
        ServiceGenerator.createRemoteService(RemoteService::class.java)
            .saveToFavorite(wrapper)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                if (it.success) {
                    detailView?.onFavoriteSaved(true)
                } else {
                    detailView?.onFavoriteSaved(false)
                }
            }
    }

    override fun deleteFromFavorite(thumbnailUrl: String) {
        ServiceGenerator.createRemoteService(RemoteService::class.java)
            .deleteFromFavorite("token", thumbnailUrl)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                if (it.success) {
                    detailView?.onFavoriteDelete(true)
                } else {
                    detailView?.onFavoriteDelete(false)
                }
            }
    }

    override fun requireFavoriteState(thumbnailUrl: String) {
        ServiceGenerator.createRemoteService(RemoteService::class.java)
            .getFavoriteState("token", thumbnailUrl)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                if (it.success) {
                    detailView?.onFavoriteStateReceived(true)
                } else {
                    detailView?.onFavoriteStateReceived(false)
                }
            }
    }

    override fun requirePreferLayoutId(isInitial: Boolean) {
        detailView as DetailFragment
        val sp = globalContext.getSharedPreferences(
            LayoutCustomActivity.SP_KEY,
            Context.MODE_PRIVATE
        )
        val id = sp.getInt(LayoutCustomActivity.ELEMENT_KEY, R.layout.fragment_detail)
        detailView?.onPreferLayoutIdReceived(id, isInitial)
    }

    override fun onDestroy() {
        detailView = null
    }

}