package com.cory.streamline.detail

import android.content.Context
import android.media.MediaScannerConnection
import android.os.Environment
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.cory.streamline.R
import com.cory.streamline.home.HomeActivity
import com.cory.streamline.login.data.model.LoggedInUser
import com.cory.streamline.model.remote.RemoteSource
import com.cory.streamline.model.ImageSource
import com.cory.streamline.model.ImageWrapper
import com.cory.streamline.setting.LayoutCustomActivity
import com.cory.streamline.util.globalContext
import com.cory.streamline.util.log
import com.cory.streamline.util.toast
import com.cory.streamline.util.user
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import java.io.File
import java.util.*

class DetailPresenter(
    @Volatile private var detailView: IDetailView?
) : IDetailPresenter {
    private lateinit var ioThread: Thread

    companion object {
        private val PICTURE_DIRECTORY =
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).path +
                    "/Streamline/"
    }

    override fun copyCache(fullSizeUrl: String) {
        if (!this::ioThread.isInitialized || !ioThread.isAlive) {
            ioThread = Thread() {
                val fragment = detailView as DetailFragment
                val context =
                    fragment.activity as AppCompatActivity? ?: return@Thread
                val file: File = Glide.with(context)
                    .asFile()
                    .load(fullSizeUrl)
                    .submit()
                    .get()
                val calendar = Calendar.getInstance()
                val date = "${calendar.get(Calendar.YEAR)}-${calendar.get(Calendar.MONTH)}" +
                        "-${calendar.get(Calendar.DAY_OF_MONTH)}"
                val newName = "${date}-${file.name.substring(0..5)}.jpg"
                val newFile = File(PICTURE_DIRECTORY + newName)
                if (!newFile.exists()) {
                    file.copyTo(newFile)
                    context.runOnUiThread {
                        MediaScannerConnection.scanFile(
                            context
                            , arrayOf(newFile.path)
                            , arrayOf("image/jpeg")
                            , null
                        )
                        fragment.onCacheCopied(newFile.path, false)
                    }
                } else {
                    context.runOnUiThread {
                        fragment.onCacheCopied(newFile.path, true)
                    }
                }
            }
            ioThread.start()
        }
    }

    override fun saveToFavorite(imageSource: ImageSource) {
        //TODO replace token use login util
        val loggedUser = user
        if (loggedUser == null) {
            toast("请先登录")
        } else {
            val wrapper =
                ImageWrapper(loggedUser.token, imageSource)
            RemoteSource.saveToFavorite(wrapper)
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
    }

    override fun deleteFromFavorite(thumbnailUrl: String) {
        //TODO replace token use login util
        val loggedUser = user
        if (loggedUser != null) {
            RemoteSource.deleteFromFavorite(loggedUser.token, thumbnailUrl)
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
    }

    override fun requireFavoriteState(thumbnailUrl: String) {
        //TODO replace token use login util
        val loggedUser = user
        if (loggedUser != null) {
            RemoteSource.getFavoriteState(loggedUser.token, thumbnailUrl)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    if (it.success) {
                        detailView?.onFavoriteStateReceived(it.isFavorite)
                    } else {
                        log("获得收藏状态：失败")
                    }
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