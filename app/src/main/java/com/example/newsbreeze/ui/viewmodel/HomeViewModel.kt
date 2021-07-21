package com.example.newsbreeze.ui.viewmodel

import android.app.Application
import android.content.Context
import androidx.lifecycle.*
import com.example.newsbreeze.data.datasource.RemoteDataSource
import com.example.newsbreeze.data.db.BreakingNewsEntity
import com.example.newsbreeze.data.db.SavedNewsEntity
import com.example.newsbreeze.data.network.response.BreakingNewsResponse
import com.example.newsbreeze.ui.MainActivity.Companion.isPrimaryAPICallDone
import com.example.newsbreeze.ui.repository.MainRepository
import com.example.newsbreeze.utils.NetworkResult
import com.example.newsbreeze.utils.Utils
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class HomeViewModel
@Inject
constructor(val repository: MainRepository, val app: Application) :
    AndroidViewModel(app) {

    val breakingNewsFromDB = repository.localDataSource.readAllBreakingNews().asLiveData()
    val savedNewsFromDB = repository.localDataSource.readAllSavedNews().asLiveData()

    val searchQuery = MutableStateFlow("")

    val searchFlow = searchQuery.flatMapLatest {
        repository.localDataSource.searchBreakingNews(it)
    }

    val searchNewsLiveData = searchFlow.asLiveData()

    val resultBreakingNews: MutableLiveData<NetworkResult<BreakingNewsResponse>> =
        MutableLiveData()

    val homeViewModelChannel = Channel<String>()
    val homeViewModelChannelFlow = homeViewModelChannel.receiveAsFlow()

    fun getBreakingNews() = viewModelScope.launch {
        makeBreakingNewsSafeCall()
    }

    suspend fun makeBreakingNewsSafeCall() {
        resultBreakingNews.value = NetworkResult.Loading()
        if (Utils.hasInternetConnection(app.applicationContext)) {
            try {
                val response = repository.remoteDataSource.makeAPICallForBreakingNews()
                resultBreakingNews.value = handleBreakingNewsResponse(response)
            } catch (e: Exception) {
                resultBreakingNews.value = NetworkResult.Error(data = null, msg = e.toString())
            }
        } else {
            resultBreakingNews.value =
                NetworkResult.Error(data = null, msg = "No Internet Connection")
        }
    }

    fun handleBreakingNewsResponse(response: Response<BreakingNewsResponse>): NetworkResult<BreakingNewsResponse> {
        return when {
            response.body() == null -> {
                NetworkResult.Error(data = null, msg = "News Unavailable")
            }
            response.message().toString().contains("timeout") -> NetworkResult.Error(
                data = null,
                msg = "Timeout. Try again."
            )
            response.code() == 401 -> NetworkResult.Error(data = null, msg = "API Key Expired")
            response.body()!!.articles.isNullOrEmpty() -> NetworkResult.Error(
                data = null,
                msg = "News Unavailable"
            )
            response.isSuccessful -> {
                isPrimaryAPICallDone = true
                val response1 = response.body()
                NetworkResult.Success(data = response1!!)
            }
            else -> {
                NetworkResult.Error(data = null, msg = "Unknown Network Error")
            }
        }
    }

    fun clearBreakingNewsTableAndAddThisList(list: List<BreakingNewsEntity>) =
        viewModelScope.launch(Dispatchers.IO) {
            repository.localDataSource.deleteAllBreakingNews()
            list.forEach {
                repository.localDataSource.insertBreakingNews(it)
            }
        }

    fun addToSavedNewsTable(entity: BreakingNewsEntity) = viewModelScope.launch(Dispatchers.IO) {
        val entity1 = SavedNewsEntity(entity.title,entity.description,entity.urlToImage,entity.content,entity.publishedAt,entity.author)
        repository.localDataSource.insertSavedNews(entity1)
        homeViewModelChannel.send("News Saved")
    }

}