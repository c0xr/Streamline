package com.cory.streamline.util

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.cory.streamline.model.exception.SourceNotFoundException
import com.cory.streamline.model.web.WebSource
import com.cory.streamline.model.web.source.WallhevenSource

private const val TAG = "StreamlineLog"
private lateinit var context: Context
const val SOURCE_WALLHAVEN = "WallhevenSource"
const val SOURCE_PIXABAY = "PixabaySource"
const val SOURCE_FREEPIK = "FreepikSource"
const val CATEGORY_LATEST = "Latest"
const val CATEGORY_POPULAR = "Popular"
private lateinit var loginContext: Context

fun log(o: Any?) {
    Log.i(TAG, o?.toString() ?: "Object logged is null")
}

fun toast(o: Any?) {
    Toast.makeText(context, o?.toString() ?: "Object toasted is null", Toast.LENGTH_LONG)
        .show()
}

fun initToast(_context: Context) {
    context = _context
}

fun initLoginContext(context: Context) {
    loginContext = context
}

fun loginContext(): Context {
    return loginContext
}

fun createWebSourceBy(sourceString: String): WebSource<*> {
    return when (sourceString) {
        SOURCE_WALLHAVEN -> WallhevenSource()
        else -> throw SourceNotFoundException(sourceString)
    }
}