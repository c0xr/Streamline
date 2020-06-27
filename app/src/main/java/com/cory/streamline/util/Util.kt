package com.cory.streamline.util

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.cory.streamline.model.Fetchable
import org.jsoup.Jsoup

private const val TAG = "StreamlineLog"
private lateinit var context: Context

fun log(o: Any?) {
    Log.i(TAG, o?.toString() ?: "Object logged is null")
}

fun toast(o: Any?) {
    Toast.makeText(context, o?.toString() ?: "Object toasted is null", Toast.LENGTH_SHORT)
        .show()
}

fun initToast(_context: Context) {
    context = _context
}