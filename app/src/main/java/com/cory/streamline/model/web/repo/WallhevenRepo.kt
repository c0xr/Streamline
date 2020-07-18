package com.cory.streamline.model.web.repo

import com.cory.streamline.model.web.ImageSource
import com.cory.streamline.model.web.OpenWebRepo
import com.cory.streamline.model.web.WebRepo

class WallhevenRepo : OpenWebRepo() {
    private lateinit var data: List<Data>

    class Data {
        lateinit var path: String
        lateinit var thumbs: Thumbs

        class Thumbs {
            lateinit var small: String
        }
    }

    override fun onCreateImageSource(position: Int): ImageSource {
        val data = data[position]
        return ImageSource(data.thumbs.small, data.path)
    }

    override fun getSize(): Int {
        return data.size
    }

}