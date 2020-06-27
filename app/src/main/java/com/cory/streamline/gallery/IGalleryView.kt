package com.cory.streamline.gallery

interface IGalleryView {
    fun onFetchingCompleted(thumbnails:List<String>)
    fun onFetchingMoreCompleted(thumbnails:List<String>)
}