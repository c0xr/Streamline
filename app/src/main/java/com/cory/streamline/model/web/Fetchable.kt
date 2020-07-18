package com.cory.streamline.model.web

import java.io.Serializable

interface Fetchable {
    fun getImageSources(): List<ImageSource>

    fun getSize(): Int
}