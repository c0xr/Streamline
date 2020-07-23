package com.cory.streamline.model.web

import com.cory.streamline.model.ImageSource

abstract class BaseWebRepo : Fetchable {
    protected val imageSources = ArrayList<ImageSource>()

}