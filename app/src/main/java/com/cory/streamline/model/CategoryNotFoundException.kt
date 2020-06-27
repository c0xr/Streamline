package com.cory.streamline.model

import java.lang.Exception

class CategoryNotFoundException(val category: String) : Exception() {
    override fun toString(): String {
        return "Category ($category) can't be parsed"
    }
}