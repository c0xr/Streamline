package com.cory.streamline.model.web

abstract class OpenWebRepo : WebRepo {
    override var imageSources = ArrayList<ImageSource>()

    override fun getImageSources(): List<ImageSource> {
        for (i in 0 until getSize()) {
            imageSources.add(onCreateImageSource(i))
        }
        return imageSources
    }

    abstract fun onCreateImageSource(position: Int): ImageSource
}