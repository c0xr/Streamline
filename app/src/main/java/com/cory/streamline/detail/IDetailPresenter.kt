package com.cory.streamline.detail

import com.cory.streamline.model.ImageSource


interface IDetailPresenter {
    fun copyCache(fullSizeUrl: String)

    fun saveToFavorite(imageSource: ImageSource)

    fun deleteFromFavorite(thumbnailUrl: String)

    fun requireFavoriteState(thumbnailUrl: String)

    fun requirePreferLayoutId(isInitial: Boolean)

    fun onDestroy()

}