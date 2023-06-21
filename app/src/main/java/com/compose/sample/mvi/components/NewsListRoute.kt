package com.compose.sample.mvi.components

import android.app.Activity
import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.compose.sample.mvi.NewsListViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.compose.sample.mvi.data.News
import com.compose.sample.mvi.data.NewsListContract
import com.compose.sample.mvi.ext.CollectInLaunchedEffect
import com.compose.sample.mvi.ext.use
import com.compose.sample.mvi.repository.newsList

@Composable
fun NewsListRoute(
    viewModel: NewsListViewModel = viewModel(),
    showFavoriteList: Boolean = false,
    onNavigateToDetailScreen: (News) -> Unit = {}
) {
    val (state, event, effect) = use(viewModel = viewModel)
    val activity = LocalContext.current as? Activity

    LaunchedEffect(Unit) {
        event.invoke(
            NewsListContract.Event.OnSetShowFavoriteList(showFavoriteList)
        )
        event.invoke(
            NewsListContract.Event.OnGetNewsList(showFavoriteList)
        )
    }

    effect.CollectInLaunchedEffect { eff ->
        when (eff) {
            NewsListContract.Effect.OnBackPressed -> activity?.onBackPressed()
            is NewsListContract.Effect.ShowToast -> {
                Toast.makeText(activity, eff.message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    NewsListScreen(newsListState = state,
        onNavigateToDetailScreen = onNavigateToDetailScreen,
        onFavoriteClick = { news ->
            event.invoke(NewsListContract.Event.OnFavoriteClick(news))
        },
        onRefresh = {
            event.invoke(NewsListContract.Event.OnRefresh)
        },
        onBackPressed = {
            event.invoke(NewsListContract.Event.OnBackPressed)
        },
        showToast = { msg ->
            event.invoke(NewsListContract.Event.ShowToast(msg))
        })
}

@Composable
private fun NewsListScreen(
    newsListState: NewsListContract.State,
    onNavigateToDetailScreen: (News) -> Unit,
    onFavoriteClick: (News) -> Unit,
    onRefresh: () -> Unit,
    onBackPressed: () -> Unit,
    showToast: (String) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Row {
            Button(onClick = onBackPressed) {
                Text(text = "Back")
            }
            Spacer(modifier = Modifier.width(16.dp))
            Button(onClick = { showToast("hiiiiii!") }) {
                Text(text = "Toast")
            }
        }
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp)
        ) {
            items(newsListState.news) { news ->
                Column {
                    Text(text = news.title)
                    Text(text = news.content)
                }
                Divider(Modifier.padding(8.dp))
            }
        }
    }
}

@Preview
@Composable
fun NewsListScreenPrev() {
    NewsListScreen(newsListState = NewsListContract.State(news = newsList),
        onNavigateToDetailScreen = {},
        onFavoriteClick = {},
        onRefresh = { /*TODO*/ },
        onBackPressed = { /*TODO*/ },
        showToast = {})
}