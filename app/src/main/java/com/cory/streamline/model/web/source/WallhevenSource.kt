package com.cory.streamline.model.web.source

import com.cory.streamline.model.web.WebSource
import com.cory.streamline.model.web.repo.WallhevenRepo
import io.reactivex.rxjava3.core.Observable

class WallhevenSource : WebSource<WallhevenRepo>(BASE_URL) {
    private var currentPageLatest = 1
    private var currentPagePopular = 1

    companion object {
        private const val BASE_URL = "https://wallhaven.cc/"
    }

    override fun getLatestImageUrls(): Observable<WallhevenRepo>? {
        return webService.getLastedResultsForWallheven(page = currentPageLatest++)
    }

    override fun getPopularImageUrls(): Observable<WallhevenRepo>? {
        return webService.getToplistResultsForWallheven(page = currentPagePopular++)
    }

}