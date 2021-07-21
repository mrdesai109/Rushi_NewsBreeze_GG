package com.example.newsbreeze.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface BreakingNewsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBreakingNews(entity: BreakingNewsEntity)

    @Query("select * from breaking_news_table")
    fun readAllBreakingNews(): Flow<List<BreakingNewsEntity>>

    @Query("select * from breaking_news_table where title like '%' || :searchQuery || '%'")
    fun searchBreakingNews(searchQuery: String): Flow<List<BreakingNewsEntity>>

    @Query("delete from breaking_news_table")
    fun deleteAllBreakingNews()
}