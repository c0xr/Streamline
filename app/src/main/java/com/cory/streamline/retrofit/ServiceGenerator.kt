package com.cory.streamline.retrofit

import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

object ServiceGenerator {
    private var modified = true

    var baseUrl = ""
        set(value) {
            field = value
            modified = true
        }

    private val builder = Retrofit.Builder()
        .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
        .addConverterFactory(GsonConverterFactory.create())

    private lateinit var retrofit: Retrofit

    fun <S> createService(serviceClass: Class<S>): S {
        if (baseUrl == "") throw IllegalStateException("Base URL is not set yet")
        if (modified) {
            retrofit = builder.baseUrl(baseUrl).build()
            modified = !modified
        }
        return retrofit.create(serviceClass)
    }
}