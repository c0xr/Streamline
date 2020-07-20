package com.cory.streamline.model.web

import java.io.Serializable

data class ImageSource(
    val thumbnailImage: String,
    val fullSizeImage: String,
    val resolution: String = "",
    val category: String = "",
    val fileSize: Int = 0,
    val sourceCreatedTime: String = "0000-00-00"
) : Serializable