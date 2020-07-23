package com.cory.streamline.model.web.repo

import com.cory.streamline.model.web.ClosedWebRepo
import org.jsoup.Jsoup

class TestClosedRepo : ClosedWebRepo() {
    override fun onLoadDocument(): String {
        return ""
    }

    override fun getSize(): Int {
        return 0
    }

    override fun extractImageUrlsFrom(docString: String): List<String> {
        val doc = Jsoup.parse(docString)
        val elements = doc.select("img[src~=(?i)\\.(jpe?g)]")
        return List(elements.size) { elements[it].attr("src") }
    }

    override fun getFullSizeUrlFrom(thumbnailUrl: String): String {
        return ""
    }

}