package com.cory.streamline.gallery

import io.reactivex.rxjava3.core.Observable
import okhttp3.ResponseBody
import retrofit2.http.GET
import retrofit2.http.Query

interface GalleryService {
    @GET("search")
    fun getThumbnails(
        @Query("query") query: String,
        @Query("page") page: Int
    ): Observable<ResponseBody>
}