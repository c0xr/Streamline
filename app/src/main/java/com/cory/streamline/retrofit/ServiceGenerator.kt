package com.cory.streamline.retrofit

import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

object ServiceGenerator {
    private var modified = true
    var webBaseUrl = ""
        set(value) {
            field = value
            modified = true
        }
    private const val remoteBaseUrl = "https://run.mocky.io/v3/"

    private val builder = Retrofit.Builder()
        .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
        .addConverterFactory(GsonConverterFactory.create())

    private lateinit var retrofitWeb: Retrofit

    private val retrofitRemote = builder.baseUrl(remoteBaseUrl).build()

    fun <S> createWebService(serviceClass: Class<S>): S {
        if (webBaseUrl == "") throw IllegalStateException("Base URL is not set yet")
        if (modified) {
            retrofitWeb = builder.baseUrl(webBaseUrl).build()
            modified = !modified
        }
        return retrofitWeb.create(serviceClass)
    }

    fun <S> createRemoteService(serviceClass: Class<S>): S {
        return retrofitRemote.create(serviceClass)
    }
}