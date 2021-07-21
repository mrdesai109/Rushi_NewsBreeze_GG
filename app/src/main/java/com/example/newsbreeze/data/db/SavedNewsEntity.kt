package com.example.newsbreeze.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "saved_news_table")
data class SavedNewsEntity(
   var title: String,
   var description: String,
   var urlToImage: String,
   var content: String,
   var publishedAt: String,
   var author: String
) {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}