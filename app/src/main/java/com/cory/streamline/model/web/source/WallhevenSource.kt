package com.cory.streamline.model.web.source

import com.cory.streamline.model.web.WebSource
import com.cory.streamline.model.web.repo.WallhevenRepo
import io.reactivex.rxjava3.core.Observable

class WallhevenSource : WebSource<WallhevenRepo>(BASE_URL) {
    companion object{
        private val BASE_URL = "https://wallhaven.cc/"
    }

    override fun getLatestImageUrls(): Observable<WallhevenRepo>? {
        return retrofitService.getLastedResultsForWallheven()
    }

    override fun getPopularImageUrls(): Observable<WallhevenRepo>? {
        return retrofitService.getLastedResultsForWallheven()
    }

}