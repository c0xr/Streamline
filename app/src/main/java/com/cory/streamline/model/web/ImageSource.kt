package com.cory.streamline.model.web

import java.io.Serializable

data class ImageSource(
    val thumbnailImage: String,
    val fullSizeImage: String
) : Serializable