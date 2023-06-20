package com.compose.sample.mvi.repository

import com.compose.sample.mvi.data.News
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class NewsRepository {


    fun getNews(): Flow<List<News>> = flowOf(
        newsList
    )

    fun toggleFavoriteNews(news: News) {}

    fun getFavoriteNews(): Flow<List<News>> = flowOf(
        newsList
    )
}

val newsList = listOf(
    News(1L, "hello", "mvi"),
    News(2L, "compose", "react UI"),
    News(3L, "android", "android 14"),
)