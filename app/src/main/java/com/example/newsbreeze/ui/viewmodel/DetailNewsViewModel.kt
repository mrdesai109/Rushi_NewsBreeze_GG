package com.example.newsbreeze.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.newsbreeze.data.db.BreakingNewsEntity
import com.example.newsbreeze.data.db.SavedNewsDao
import com.example.newsbreeze.data.db.SavedNewsEntity
import com.example.newsbreeze.ui.repository.MainRepository
import dagger.assisted.Assisted
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailNewsViewModel @Inject constructor(
    val savedNewsDao: SavedNewsDao,
    val repository: MainRepository
) : ViewModel() {

    val savedNewsLiveData = savedNewsDao.readAllSavedNews().asLiveData()

    val detailNewsViewModelChannel = Channel<String>()
    val detailNewsViewModelChannelFlow = detailNewsViewModelChannel.receiveAsFlow()

    fun addToSavedNewsTable(entity: SavedNewsEntity) = viewModelScope.launch(Dispatchers.IO) {
        val entity1 = SavedNewsEntity(entity.title,entity.description,entity.urlToImage,entity.content,entity.publishedAt,entity.author)
        repository.localDataSource.insertSavedNews(entity1)
        detailNewsViewModelChannel.send("News Saved")
    }
}