package com.cory.streamline.model.remote

import com.cory.streamline.model.web.Fetchable
import com.cory.streamline.model.web.ImageSource
import com.cory.streamline.model.web.ImageWrapper
import com.cory.streamline.model.web.WebSource
import com.cory.streamline.model.web.service.WebService
import com.cory.streamline.retrofit.ServiceGenerator
import io.reactivex.rxjava3.core.Observable

object RemoteSource : WebSource<RemoteRepo, RemoteService>(RemoteService::class.java) {
    override fun getFavoriteImageUrls(): Observable<RemoteRepo>? {
        return service.getFavoriteRecords("token")
    }

    override fun getHistoryImageUrls(): Observable<RemoteRepo>? {
        return service.getHistoryRecords("token")
    }

    fun saveToHistory(imageWrapper: ImageWrapper): Observable<RemoteResponse> {
        return service.saveToHistory(imageWrapper)
    }
}