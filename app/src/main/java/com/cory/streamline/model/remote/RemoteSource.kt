package com.cory.streamline.model.remote

import com.cory.streamline.model.ImageWrapper
import com.cory.streamline.model.RemoteResponse
import com.cory.streamline.model.web.WebSource
import io.reactivex.rxjava3.core.Observable

object RemoteSource : WebSource<RemoteRepo, RemoteService>(RemoteService::class.java) {
    private var currentPageFavorite = 1
    private var currentPageHistory = 1

    override fun getFavoriteImageUrls(token: String): Observable<RemoteRepo>? {
        //TODO replace token use login util
        return service.getFavoriteRecords(currentPageFavorite++, token)
    }

    override fun getHistoryImageUrls(token: String): Observable<RemoteRepo>? {
        //TODO replace token use login util
        return service.getHistoryRecords(currentPageHistory++, token)
    }

    fun saveToFavorite(imageWrapper: ImageWrapper): Observable<RemoteResponse> {
        return service.saveToFavorite(imageWrapper)
    }

    fun deleteFromFavorite(token: String, thumbnail: String): Observable<RemoteResponse> {
        return service.deleteFromFavorite(token, thumbnail)
    }

    fun getFavoriteState(token: String, thumbnail: String): Observable<RemoteResponse> {
        return service.getFavoriteState(token, thumbnail)
    }

    fun saveToHistory(imageWrapper: ImageWrapper): Observable<RemoteResponse> {
        return service.saveToHistory(imageWrapper)
    }

    fun reset() {
        currentPageHistory = 1
        currentPageFavorite = 1
    }
}