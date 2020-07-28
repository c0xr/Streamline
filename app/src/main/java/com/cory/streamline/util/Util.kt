package com.cory.streamline.util

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.cory.streamline.login.data.model.LoggedInUser
import com.cory.streamline.model.exception.SourceNotFoundException
import com.cory.streamline.model.remote.RemoteSource
import com.cory.streamline.model.web.WebSource
import com.cory.streamline.model.web.source.WallhevenSource
import java.lang.ref.WeakReference

private const val TAG = "StreamlineLog"
private lateinit var globalContextR: WeakReference<Context>
var globalContext
    get() = globalContextR.get()!!
    set(value) {
        globalContextR = WeakReference(value)
    }
var user: LoggedInUser? = null
const val SOURCE_WALLHAVEN = "WallhevenSource"
const val SOURCE_PIXABAY = "PixabaySource"
const val SOURCE_FREEPIK = "FreepikSource"
const val SOURCE_UNSPLASH = "UnsplashSource"
const val SOURCE_FAVORITE = "FavoriteSource"
const val SOURCE_HISTORY = "HistorySource"
const val CATEGORY_LATEST = "Latest"
const val CATEGORY_POPULAR = "Popular"
const val CATEGORY_FAVORITE = "FavoriteSource"
const val CATEGORY_HISTORY = "HistorySource"

fun log(o: Any?) {
    Log.i(TAG, o?.toString() ?: "Object logged is null")
}

fun toast(o: Any?) {
    Toast.makeText(globalContext, o?.toString() ?: "Object toasted is null", Toast.LENGTH_SHORT)
        .show()
}

fun createWebSourceBy(sourceString: String): WebSource<*, *> {
    return when (sourceString) {
        SOURCE_WALLHAVEN -> WallhevenSource()
        SOURCE_HISTORY -> {
            RemoteSource.reset()
            RemoteSource
        }
        SOURCE_FAVORITE -> {
            RemoteSource.reset()
            RemoteSource
        }
        else -> throw SourceNotFoundException(sourceString)
    }
}