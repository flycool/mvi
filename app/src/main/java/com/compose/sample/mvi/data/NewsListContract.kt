package com.compose.sample.mvi.data

import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow

interface NewsListContract :
    UnidirectionalViewModel<NewsListContract.State, NewsListContract.Event, NewsListContract.Effect> {

    data class State(
        val news: List<News> = listOf(),
        val refreshing: Boolean = false,
        val showFavoriteList: Boolean = false,
        val error: String? = null,
    )

    sealed class Event {
        data class OnFavoriteClick(val news: News) : Event()
        data class OnGetNewsList(val showFavoriteList: Boolean) : Event()
        data class OnSetShowFavoriteList(val showFavoriteList: Boolean) : Event()
        object OnRefresh : Event()
        object OnBackPressed : Event()
        data class ShowToast(val message: String) : Event()
    }

    sealed class Effect {
        object OnBackPressed : Effect()
        data class ShowToast(val message: String) : Effect()
    }
}


interface UnidirectionalViewModel<STATE, EVENT, EFFECT> {
    val state: StateFlow<STATE>
    val effect: SharedFlow<EFFECT>
    fun event(event: EVENT)
}