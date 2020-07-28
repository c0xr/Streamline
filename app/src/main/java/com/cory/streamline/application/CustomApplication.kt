package com.cory.streamline.application

import android.app.Application
import com.cory.streamline.util.globalContext
import com.cory.streamline.util.toast
import io.reactivex.rxjava3.exceptions.UndeliverableException
import io.reactivex.rxjava3.plugins.RxJavaPlugins
import retrofit2.adapter.rxjava3.HttpException
import java.io.IOException
import java.net.SocketException

class CustomApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        globalContext = applicationContext
        RxJavaPlugins.setErrorHandler {
            when (it) {
                is UndeliverableException -> return@setErrorHandler
                is IOException -> return@setErrorHandler
                is SocketException -> return@setErrorHandler
                is HttpException -> return@setErrorHandler
            }
        }
    }
}