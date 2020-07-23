package com.cory.streamline.model.exception

import java.lang.Exception

class SourceNotFoundException(private val sourceString: String) : Exception() {
    override fun toString(): String {
        return "SourceString $sourceString can't be parsed"
    }
}