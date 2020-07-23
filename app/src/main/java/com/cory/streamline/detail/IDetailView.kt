package com.cory.streamline.detail

interface IDetailView {
    fun onCacheCopied(filePath: String, success: Boolean)

    fun onFavoriteSaved(success: Boolean)

    fun onFavoriteDelete(success: Boolean)

    fun onFavoriteStateReceived(isFavorite: Boolean)

    fun onPreferLayoutIdReceived(preferLayoutId: Int, isInitial: Boolean)

}