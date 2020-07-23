package com.cory.streamline.model.web

import com.cory.streamline.model.ImageSource

interface Fetchable {
    fun getImageSources(): List<ImageSource>

    fun getSize(): Int
}