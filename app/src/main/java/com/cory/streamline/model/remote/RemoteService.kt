package com.cory.streamline.model.remote

import com.cory.streamline.model.web.repo.WallhevenRepo
import io.reactivex.rxjava3.core.Observable
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface RemoteService {
    @POST("4836f9ad-333a-4409-b54a-80e9a2e0ed0d")
    fun saveToFavorite(): Observable<RemoteResponse>
}