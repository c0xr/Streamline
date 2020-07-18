package com.cory.streamline.model.web.source

import com.cory.streamline.model.web.WebSource
import com.cory.streamline.model.web.repo.TestClosedRepo

class TestWebSource : WebSource<TestClosedRepo>(BASE_URL) {
    companion object{
        private val BASE_URL = "https://wallhaven.cc/"
    }

}