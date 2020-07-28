package com.cory.streamline.model.remote

import com.cory.streamline.model.ImageWrapper
import com.cory.streamline.model.RemoteResponse
import io.reactivex.rxjava3.core.Observable
import retrofit2.http.*

interface RemoteService {
    @POST("Streamline/SaveToFavoriteServlet")
    fun saveToFavorite(
        @Body imageWrapper: ImageWrapper
    ): Observable<RemoteResponse>

    @FormUrlEncoded
    @POST("Streamline/DeleteFromFavoriteServlet")
    fun deleteFromFavorite(
        @Field("token") token: String,
        @Field("thumbnailUrl") thumbnailUrl: String
    ): Observable<RemoteResponse>

    @FormUrlEncoded
    @POST("Streamline/GetFavoriteState")
    fun getFavoriteState(
        @Field("token") token: String,
        @Field("thumbnailUrl") thumbnailUrl: String
    ): Observable<RemoteResponse>

    @POST("Streamline/SaveToHistoryServlet")
    fun saveToHistory(
        @Body imageWrapper: ImageWrapper
    ): Observable<RemoteResponse>

    @FormUrlEncoded
    @POST("Streamline/GetFavoriteRecords")
    fun getFavoriteRecords(
        @Query("page") page: Int,
        @Field("token") token: String
    ): Observable<RemoteRepo>

    @FormUrlEncoded
    @POST("Streamline/GetHistoryRecords")
    fun getHistoryRecords(
        @Query("page") page: Int,
        @Field("token") token: String
    ): Observable<RemoteRepo>

}