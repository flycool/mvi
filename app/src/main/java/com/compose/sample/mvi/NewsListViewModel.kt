package com.compose.sample.mvi

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.compose.sample.mvi.data.News
import com.compose.sample.mvi.data.NewsListContract
import com.compose.sample.mvi.repository.NewsRepository
import com.compose.sample.mvi.repository.newsList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class NewsListViewModel(
    private val newsRepository: NewsRepository = NewsRepository()
) : ViewModel(), NewsListContract {

    private val _newsState = MutableStateFlow(NewsListContract.State())
    override val state: StateFlow<NewsListContract.State> = _newsState.asStateFlow()

    private val _effect = MutableSharedFlow<NewsListContract.Effect>()
    override val effect: SharedFlow<NewsListContract.Effect> = _effect.asSharedFlow()

    override fun event(event: NewsListContract.Event) = when (event) {
        NewsListContract.Event.OnBackPressed -> onBackPressed()
        is NewsListContract.Event.OnRefresh -> getData(isRefreshing = true)
        is NewsListContract.Event.OnFavoriteClick -> onFavoriteClick(event.news)
        is NewsListContract.Event.OnGetNewsList -> getData(showFavoriteList = _newsState.value.showFavoriteList)
        is NewsListContract.Event.OnSetShowFavoriteList -> onSetShowFavoriteList(event.showFavoriteList)
        is NewsListContract.Event.ShowToast -> showToast(event.message)
    }

    private fun getData(
        isRefreshing: Boolean = false,
        showFavoriteList: Boolean = false,
    ) {
        if (isRefreshing) {
            _newsState.update { it.copy(refreshing = true) }
        }
        viewModelScope.launch {
            if (showFavoriteList) {
                getFavoriteNews()
            } else {
                getNewsList()
            }
        }
    }

    private fun onSetShowFavoriteList(showFavoriteList: Boolean) {
        _newsState.update { it.copy(showFavoriteList = showFavoriteList) }
    }

    private fun getNewsList() = newsRepository.getNews()
        .catch { ex ->
            _newsState.update { it.copy(error = ex.message ?: "An unexpected error occurred") }
        }
        .onEach { newsList ->
            _newsState.update { it.copy(news = newsList) }
        }.launchIn(viewModelScope)

    private fun getFavoriteNews() = newsRepository.getFavoriteNews()
        .onEach { newsList ->
            _newsState.update { it.copy(news = newsList) }
        }.launchIn(viewModelScope)

    private fun onFavoriteClick(news: News) {
        viewModelScope.launch(Dispatchers.IO) {
            newsRepository.toggleFavoriteNews(news)
        }
    }

    private fun onBackPressed() {
        viewModelScope.launch {
            _effect.emit(NewsListContract.Effect.OnBackPressed)
        }
    }

    private fun showToast(message: String) {
        viewModelScope.launch {
            _effect.emit(NewsListContract.Effect.ShowToast(message))
        }
    }


}