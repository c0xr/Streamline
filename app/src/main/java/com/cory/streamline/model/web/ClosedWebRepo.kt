package com.cory.streamline.model.web

abstract class ClosedWebRepo : WebRepo, Extractable {
    override var imageSources = ArrayList<ImageSource>()
    private lateinit var docString: String

    override fun getImageSources(): List<ImageSource> {
        docString = onLoadDocument()
        val thumbnailUrls = extractImageUrlsFrom(docString)
        val fullSizeUrls = thumbnailUrls.map { getFullSizeUrlFrom(it) }
        val imageSources = ArrayList<ImageSource>()
        thumbnailUrls.zip(fullSizeUrls) { thumb, full ->
            imageSources.add(ImageSource(thumb, full))
        }
        return imageSources
    }

    abstract fun onLoadDocument(): String
}