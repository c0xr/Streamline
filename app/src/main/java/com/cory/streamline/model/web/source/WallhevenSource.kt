package com.cory.streamline.model.web.source

import com.cory.streamline.model.web.WebSource
import com.cory.streamline.model.web.repo.WallhevenRepo
import com.cory.streamline.model.web.service.WebService
import io.reactivex.rxjava3.core.Observable

private const val BASE_URL = "https://wallhaven.cc/"

class WallhevenSource : WebSource<WallhevenRepo, WebService>(WebService::class.java, BASE_URL) {
    private var currentPageLatest = 1
    private var currentPagePopular = 1

    override fun getLatestImageUrls(): Observable<WallhevenRepo>? {
        return service.getLastedResultsForWallheven(page = currentPageLatest++)
    }

    override fun getPopularImageUrls(): Observable<WallhevenRepo>? {
        return service.getToplistResultsForWallheven(page = currentPagePopular++)
    }

}