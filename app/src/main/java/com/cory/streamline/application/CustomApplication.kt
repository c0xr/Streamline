package com.cory.streamline.application

import android.app.Application
import com.cory.streamline.util.globalContext

class CustomApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        globalContext = applicationContext
    }
}