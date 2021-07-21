package com.example.newsbreeze.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface SavedNewsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSavedNews(entity: SavedNewsEntity)

    @Query("select * from saved_news_table")
    fun readAllSavedNews(): Flow<List<SavedNewsEntity>>

    @Query("select * from saved_news_table where title like '%' || :searchQuery || '%'")
    fun searchSavedNews(searchQuery: String): Flow<List<SavedNewsEntity>>

    @Query("delete from saved_news_table where id like :passed_id")
    fun deleteSavedNews(passed_id: Int)
}