package com.cory.streamline.model.web.service

import com.cory.streamline.model.web.repo.WallhevenRepo
import io.reactivex.rxjava3.core.Observable
import okhttp3.ResponseBody
import retrofit2.http.GET
import retrofit2.http.Query

interface WebService {
    @GET("search")
    fun getThumbnails(
        @Query("query") query: String,
        @Query("page") page: Int
    ): Observable<ResponseBody>

    @GET("api/v1/search")
    fun getLastedResultsForWallheven(
        @Query("page") page: Int = 1
    ): Observable<WallhevenRepo>

    @GET("api/v1/search")
    fun getToplistResultsForWallheven(
        @Query("sorting") category: String = "toplist",
        @Query("page") page: Int = 1
    ): Observable<WallhevenRepo>

}