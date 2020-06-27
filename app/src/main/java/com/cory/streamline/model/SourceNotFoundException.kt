package com.cory.streamline.model

import java.lang.Exception

class SourceNotFoundException(val sourceString: String) : Exception() {
    override fun toString(): String {
        return "SourceString ($sourceString) can't be parsed"
    }
}