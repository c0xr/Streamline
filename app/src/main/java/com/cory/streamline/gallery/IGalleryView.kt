package com.cory.streamline.gallery

import com.cory.streamline.model.ImageSource

interface IGalleryView {
    fun onImagesFetched(imageSources: List<ImageSource>)

    fun onMoreImagesFetched(imageSources: List<ImageSource>)

}