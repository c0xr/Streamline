package com.cory.streamline.model.web

import com.cory.streamline.model.web.service.WebService
import com.cory.streamline.retrofit.ServiceGenerator
import io.reactivex.rxjava3.core.Observable

abstract class WebSource<T : Fetchable>(baseUrl: String) {
    protected val retrofitService: WebService

    init {
        ServiceGenerator.baseUrl = baseUrl
        retrofitService = ServiceGenerator.createService(WebService::class.java)
    }

    open fun getLatestImageUrls(): Observable<T>? {
        return null
    }

    open fun getPopularImageUrls(): Observable<T>? {
        return null
    }
}