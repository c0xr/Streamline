package com.cory.streamline.model.web

import com.cory.streamline.model.remote.RemoteService
import com.cory.streamline.retrofit.ServiceGenerator
import io.reactivex.rxjava3.core.Observable

abstract class WebSource<T : Fetchable, out S>(serviceClass: Class<S>, baseUrl: String = "") {
    protected val service: S = if (serviceClass.equals(RemoteService::class.java)) {
        ServiceGenerator.createRemoteService(serviceClass)
    } else {
        ServiceGenerator.webBaseUrl = baseUrl
        ServiceGenerator.createWebService(serviceClass)
    }

    open fun getLatestImageUrls(): Observable<T>? {
        return null
    }

    open fun getPopularImageUrls(): Observable<T>? {
        return null
    }

    open fun getFavoriteImageUrls(token: String): Observable<T>? {
        return null
    }

    open fun getHistoryImageUrls(token: String): Observable<T>? {
        return null
    }
}