package com.cory.streamline.gallery

interface IGalleryPresenter {
    fun fetchThumbnails()
    fun fetchMoreThumbnails()
    fun clearReference()
}