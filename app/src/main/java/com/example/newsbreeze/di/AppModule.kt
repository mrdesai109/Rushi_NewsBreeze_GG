package com.example.newsbreeze.di

import android.content.Context
import androidx.room.Room
import com.example.newsbreeze.data.db.MasterDB
import com.example.newsbreeze.data.network.BreakingNewsAPI
import com.example.newsbreeze.utils.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun providesClient(): OkHttpClient {
        val interceptor = HttpLoggingInterceptor()
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
        val client: OkHttpClient = OkHttpClient.Builder().addInterceptor(interceptor).build()
        return client
    }

    @Singleton
    @Provides
    fun providesRetrofit(client: OkHttpClient) = Retrofit.Builder().baseUrl(Constants.BASE_URL)
        .client(client)
        .addConverterFactory(GsonConverterFactory.create()).build()

    @Singleton
    @Provides
    fun provideBreakingNewsAPI(retrofit: Retrofit) = retrofit.create(BreakingNewsAPI::class.java)

    @Singleton
    @Provides
    fun provideDB(@ApplicationContext ctx: Context) = Room.databaseBuilder(ctx,MasterDB::class.java,"news_db")
        .fallbackToDestructiveMigration()
        .build()

    @Singleton
    @Provides
    fun provideBreakingNewsDao(db: MasterDB) = db.getBreakingNewsDao()

    @Singleton
    @Provides
    fun provideSavedNewsDao(db: MasterDB) = db.getSavedNewsDao()

}