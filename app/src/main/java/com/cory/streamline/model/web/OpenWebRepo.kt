package com.cory.streamline.model.web

import com.cory.streamline.model.ImageSource

abstract class OpenWebRepo : BaseWebRepo() {
    override fun getImageSources(): List<ImageSource> {
        for (i in 0 until getSize()) {
            imageSources.add(onCreateImageSource(i))
        }
        return imageSources
    }

    abstract fun onCreateImageSource(position: Int): ImageSource
}