package com.example.newsbreeze.data.datasource

import com.example.newsbreeze.data.network.BreakingNewsAPI
import com.example.newsbreeze.data.network.response.BreakingNewsResponse
import retrofit2.Response
import javax.inject.Inject

class RemoteDataSource @Inject constructor(
    val breakingNewsAPI: BreakingNewsAPI
) {
    suspend fun makeAPICallForBreakingNews(): Response<BreakingNewsResponse> {
        return breakingNewsAPI.makeAPICallForBreakingNews()
    }
}