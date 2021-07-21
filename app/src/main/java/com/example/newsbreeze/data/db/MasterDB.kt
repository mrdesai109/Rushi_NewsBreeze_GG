package com.example.newsbreeze.data.db

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [BreakingNewsEntity::class,SavedNewsEntity::class],version = 1)
abstract class MasterDB : RoomDatabase(){

    abstract fun getBreakingNewsDao() : BreakingNewsDao

    abstract fun getSavedNewsDao() : SavedNewsDao

}