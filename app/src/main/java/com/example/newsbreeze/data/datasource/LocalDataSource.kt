package com.example.newsbreeze.data.datasource

import com.example.newsbreeze.data.db.BreakingNewsDao
import com.example.newsbreeze.data.db.BreakingNewsEntity
import com.example.newsbreeze.data.db.SavedNewsDao
import com.example.newsbreeze.data.db.SavedNewsEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LocalDataSource @Inject constructor(
    val breakingNewsDao: BreakingNewsDao,
    val savedNewsDao: SavedNewsDao
) {

    suspend fun insertBreakingNews(news : BreakingNewsEntity){
        breakingNewsDao.insertBreakingNews(news)
    }

    suspend fun deleteAllBreakingNews(){
        breakingNewsDao.deleteAllBreakingNews()
    }

    fun readAllBreakingNews() : Flow<List<BreakingNewsEntity>>{
        return breakingNewsDao.readAllBreakingNews()
    }

    fun searchBreakingNews(q: String) : Flow<List<BreakingNewsEntity>>{
        return breakingNewsDao.searchBreakingNews(searchQuery = q)
    }

    //saved news
    suspend fun insertSavedNews(news: SavedNewsEntity){
        savedNewsDao.insertSavedNews(news)
    }

    suspend fun deleteASavedNews(news: SavedNewsEntity){
        savedNewsDao.deleteSavedNews(news.id)
    }

    fun readAllSavedNews(): Flow<List<SavedNewsEntity>> {
        return savedNewsDao.readAllSavedNews()
    }

    fun searchSavedNews(q: String) : Flow<List<SavedNewsEntity>>{
        return savedNewsDao.searchSavedNews(q)
    }
}