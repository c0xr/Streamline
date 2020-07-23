package com.cory.streamline.model.remote

import com.cory.streamline.model.ImageWrapper
import com.cory.streamline.model.RemoteResponse
import com.cory.streamline.model.web.WebSource
import io.reactivex.rxjava3.core.Observable

object RemoteSource : WebSource<RemoteRepo, RemoteService>(RemoteService::class.java) {
    override fun getFavoriteImageUrls(): Observable<RemoteRepo>? {
        //TODO replace token use login util
        return service.getFavoriteRecords("token")
    }

    override fun getHistoryImageUrls(): Observable<RemoteRepo>? {
        //TODO replace token use login util
        return service.getHistoryRecords("token")
    }

    fun saveToFavorite(imageWrapper: ImageWrapper): Observable<RemoteResponse> {
        return service.saveToFavorite(imageWrapper)
    }

    fun deleteFromFavorite(token:String,thumbnail:String): Observable<RemoteResponse> {
        return service.deleteFromFavorite(token,thumbnail)
    }

    fun getFavoriteState(token:String,thumbnail:String): Observable<RemoteResponse> {
        return service.getFavoriteState(token,thumbnail)
    }

    fun saveToHistory(imageWrapper: ImageWrapper): Observable<RemoteResponse> {
        return service.saveToHistory(imageWrapper)
    }
}