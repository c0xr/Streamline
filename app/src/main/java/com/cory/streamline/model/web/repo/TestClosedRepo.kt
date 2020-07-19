package com.cory.streamline.model.web.repo

import com.cory.streamline.model.web.ClosedWebRepo
import com.cory.streamline.model.web.ImageSource
import org.jsoup.Jsoup

class TestClosedRepo : ClosedWebRepo() {
    override fun onLoadDocument(): String {
        TODO("Not yet implemented")
    }

    override fun getSize(): Int {
        TODO("Not yet implemented")
    }

    override fun extractImageUrlsFrom(docString: String): List<String> {
        val doc = Jsoup.parse(docString)
        val elements = doc.select("img[src~=(?i)\\.(jpe?g)]")
        return List(elements.size) { elements[it].attr("src") }
    }

    override fun getFullSizeUrlFrom(thumbnailUrl: String): String {
        TODO("Not yet implemented")
    }

}