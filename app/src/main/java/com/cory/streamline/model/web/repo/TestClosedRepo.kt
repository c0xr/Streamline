package com.cory.streamline.model.web.repo

import com.cory.streamline.model.web.ClosedWebRepo
import com.cory.streamline.model.web.ImageSource

class TestClosedRepo : ClosedWebRepo() {
    override fun onLoadDocument(): String {
        TODO("Not yet implemented")
    }

    override fun getSize(): Int {
        TODO("Not yet implemented")
    }

    override fun extractImageUrlsFrom(docString: String): List<String> {
        TODO("Not yet implemented")
    }

    override fun getFullSizeUrlFrom(thumbnailUrl: String): String {
        TODO("Not yet implemented")
    }

}