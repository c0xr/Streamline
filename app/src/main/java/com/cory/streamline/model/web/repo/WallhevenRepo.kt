package com.cory.streamline.model.web.repo

import com.cory.streamline.model.web.ImageSource
import com.cory.streamline.model.web.OpenWebRepo

class WallhevenRepo : OpenWebRepo() {
    private lateinit var data: List<Data>

    private class Data {
        lateinit var path: String
        lateinit var thumbs: Thumbs
        lateinit var resolution: String
        lateinit var category: String
        var file_size: Int = 0

        class Thumbs {
            lateinit var small: String
        }
    }

    override fun onCreateImageSource(position: Int): ImageSource {
        val data = data[position]
        return ImageSource(
            thumbnailImage = data.thumbs.small,
            fullSizeImage = data.path,
            resolution = data.resolution,
            category = data.category,
            fileSize = data.file_size
        )
    }

    override fun getSize(): Int {
        return data.size
    }

}