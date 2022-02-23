package com.raywenderlich.android.majesticreader.data

import com.raywenderlich.android.majesticreader.domain.Bookmark
import com.raywenderlich.android.majesticreader.domain.Document

interface DocumentDataSource {
    suspend fun add(document: Document)
    suspend fun readAll(): List<Bookmark>
    suspend fun delete(bookmark: Bookmark)
}