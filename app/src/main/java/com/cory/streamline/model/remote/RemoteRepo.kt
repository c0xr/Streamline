package com.cory.streamline.model.remote

import com.cory.streamline.model.web.ImageSource
import com.cory.streamline.model.web.ImageWrapper
import com.cory.streamline.model.web.OpenWebRepo

class RemoteRepo : OpenWebRepo() {
    private lateinit var data: List<ImageSource>

    override fun onCreateImageSource(position: Int): ImageSource {
        return data[position]
    }

    override fun getSize(): Int {
        return data.size
    }

}