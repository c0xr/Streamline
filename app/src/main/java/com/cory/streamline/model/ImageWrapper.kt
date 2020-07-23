package com.cory.streamline.model

import com.cory.streamline.model.ImageSource

data class ImageWrapper(
    val token: String,
    val imageSource: ImageSource
)