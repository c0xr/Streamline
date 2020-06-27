package com.cory.streamline.model

interface Fetchable {
    fun getBaseUrl(): String
    fun getLatestPara(): String
    fun getPopularPara(): String
    fun extractImagesFrom(docString: String): List<String>
}