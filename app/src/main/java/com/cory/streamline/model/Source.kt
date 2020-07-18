package com.cory.streamline.model

const val SOURCE_WALLHAVEN = "WallhevenSource"
const val SOURCE_PIXABAY = "PixabaySource"
const val SOURCE_FREEPIK = "FreepikSource"
const val CATEGORY_LATEST = "Latest"
const val CATEGORY_POPULAR = "Popular"

//object WallhevenSource : Fetchable {
//    private const val BASE_URL = "https://wallhaven.cc/"
//
//    override fun getBaseUrl(): String = BASE_URL
//
//    override fun getLatestPara() = ""
//
//    override fun getPopularPara() = ""
//
//    override fun extractImagesFrom(docString: String): List<String> {
//        val doc = Jsoup.parse(docString)
//        val elements = doc.select("img[src~=(?i)\\.(png|jpe?g)]")
//        return List(elements.size) { elements[it].attr("src") }
//    }
//
//    override fun getFullSizeUrl(thumbnailUrl: String) = ""
//}
//
//object PixabaySource : Available {
//    private const val BASE_URL = "https://pixabay.com/"
//
//    override fun getBaseUrl(): String = BASE_URL
//
//    override fun getLatestPara() = ""
//
//    override fun getPopularPara() = ""
//
//    override fun extractImagesFrom(docString: String): List<String> {
//        val doc = Jsoup.parse(docString)
//        val elements = doc.select("img[src~=(?i)\\.(png|jpe?g)]")
//        return List(elements.size) { elements[it].attr("src") }
//    }
//
//    override fun getFullSizeUrl(thumbnailUrl: String) = ""
//}
//
//object FreepikSource : Available {
//    private const val BASE_URL = "https://www.freepik.com/"
//
//    override fun getBaseUrl(): String = BASE_URL
//
//    override fun getLatestPara() = ""
//
//    override fun getPopularPara() = "wallpaper"
//
//    override fun extractImagesFrom(docString: String): List<String> {
//        val doc = Jsoup.parse(docString)
//        val elements = doc.select("img[src~=(?i)\\.(jpe?g)]")
//        return List(elements.size) { elements[it].attr("src") }
//    }
//
//    override fun getFullSizeUrl(thumbnailUrl: String): String {
//        return ""
//    }
//}