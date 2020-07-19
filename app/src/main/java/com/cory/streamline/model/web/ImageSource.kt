package com.cory.streamline.model.web

import java.io.Serializable

data class ImageSource(
    val thumbnailImage: String,
    val fullSizeImage: String,
    val resolution: String,
    val category: String,
    val fileSize: Int
) : Serializable {
    constructor(
        thumbnailImage: String,
        fullSizeImage: String
    ) : this(
        thumbnailImage,
        fullSizeImage,
        resolution = "",
        category = "",
        fileSize = 0
    )
}