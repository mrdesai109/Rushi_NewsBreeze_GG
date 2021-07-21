package com.example.newsbreeze.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.newsbreeze.data.db.SavedNewsEntity
import com.example.newsbreeze.ui.repository.MainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SavedNewsViewModel @Inject constructor(val repository: MainRepository, val app: Application) :
    AndroidViewModel(app) {

    val savedNews = repository.localDataSource.readAllSavedNews().asLiveData()

    val searchQuery = MutableStateFlow("")

    val searchFlow = searchQuery.flatMapLatest {
        repository.localDataSource.searchSavedNews(it)
    }

    val searchNewsLivedata = searchFlow.asLiveData()

    val savedViewModelChannel = Channel<String>()
    val savedNewsViewModelChannelFlow = savedViewModelChannel.receiveAsFlow()

    fun deleteSavedNews(currentItem: SavedNewsEntity) = viewModelScope.launch(Dispatchers.IO) {
        repository.localDataSource.deleteASavedNews(currentItem)
        savedViewModelChannel.send("Delete Success")
    }
}