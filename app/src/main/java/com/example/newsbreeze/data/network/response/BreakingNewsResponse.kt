package com.example.newsbreeze.data.network.response

data class BreakingNewsResponse(
    val articles: List<Article>,
    val status: String,
    val totalResults: Int
)