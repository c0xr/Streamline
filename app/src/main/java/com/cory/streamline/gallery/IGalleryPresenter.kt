package com.cory.streamline.gallery

interface IGalleryPresenter {
    fun fetchingImages(isInitial: Boolean)

    fun onDestroy()

}