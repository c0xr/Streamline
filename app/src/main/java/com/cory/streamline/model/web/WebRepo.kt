package com.cory.streamline.model.web

interface WebRepo {
    var imageSources: ArrayList<ImageSource>

    fun getImageSources(): List<ImageSource>
    fun getSize(): Int
}