package com.example.newsbreeze.ui.repository

import com.example.newsbreeze.data.datasource.LocalDataSource
import com.example.newsbreeze.data.datasource.RemoteDataSource
import javax.inject.Inject

class MainRepository @Inject constructor(
    remoteDataSource1: RemoteDataSource,
    localDataSource1: LocalDataSource
) {
    val remoteDataSource = remoteDataSource1
    val localDataSource = localDataSource1
}