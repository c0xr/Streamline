package com.cory.streamline.model.exception

import java.lang.Exception

class CategoryNotFoundException(private val category: String) : Exception() {
    override fun toString(): String {
        return "Category $category can't be parsed"
    }
}