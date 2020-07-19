package com.cory.streamline.gallery

interface IGalleryPresenter {
    fun getLatestResults(isInitial: Boolean)

    fun getPopularResults(isInitial: Boolean)

    fun onDestroy()

}