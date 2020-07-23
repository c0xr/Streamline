package com.cory.streamline.model.remote

import com.cory.streamline.model.web.ImageSource
import com.cory.streamline.model.web.ImageWrapper
import com.cory.streamline.model.web.repo.WallhevenRepo
import io.reactivex.rxjava3.core.Observable
import retrofit2.http.*

interface RemoteService {
    @POST("4836f9ad-333a-4409-b54a-80e9a2e0ed0d")
    fun saveToFavorite(
        @Body imageWrapper: ImageWrapper
    ): Observable<RemoteResponse>

    @FormUrlEncoded
    @POST("4836f9ad-333a-4409-b54a-80e9a2e0ed0d")
    fun deleteFromFavorite(
        @Field("token") token: String,
        @Field("thumbnailUrl") thumbnailUrl: String
    ): Observable<RemoteResponse>

    @FormUrlEncoded
    @POST("4836f9ad-333a-4409-b54a-80e9a2e0ed0d")
    fun getFavoriteState(
        @Field("token") token: String,
        @Field("thumbnailUrl") thumbnailUrl: String
    ): Observable<RemoteResponse>

    @POST("4836f9ad-333a-4409-b54a-80e9a2e0ed0d")
    fun saveToHistory(
        @Body imageWrapper: ImageWrapper
    ): Observable<RemoteResponse>

    @FormUrlEncoded
    @POST("dc5b30e9-1d56-4402-b671-60a1075400ff")
    fun getFavoriteRecords(
        @Field("token") token: String
    ): Observable<RemoteRepo>

    @FormUrlEncoded
    @POST("1724e509-f7a4-466f-8f26-c64f440b7131")
    fun getHistoryRecords(
        @Field("token") token: String
    ): Observable<RemoteRepo>

}