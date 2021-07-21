package com.example.newsbreeze.data.network

import com.example.newsbreeze.data.network.response.BreakingNewsResponse
import com.example.newsbreeze.utils.Constants
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface BreakingNewsAPI {

    @GET("v2/top-headlines")
    suspend fun makeAPICallForBreakingNews(
        @Query("apiKey") apiKey: String = Constants.API_KEY,
        @Query("country") country: String = "us",
        @Query("pageSize") pageSize: Int = 30
    ): Response<BreakingNewsResponse>
}