package com.cory.streamline.model.web.source

import com.cory.streamline.model.web.WebSource
import com.cory.streamline.model.web.repo.TestClosedRepo
import com.cory.streamline.model.web.service.WebService

private const val BASE_URL = "https://wallhaven.cc/"

class TestWebSource : WebSource<TestClosedRepo, WebService>(WebService::class.java, BASE_URL) {
}