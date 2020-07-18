package com.cory.streamline.model.web

interface Extractable {
    fun extractImageUrlsFrom(docString: String): List<String>

    fun getFullSizeUrlFrom(thumbnailUrl: String): String
}