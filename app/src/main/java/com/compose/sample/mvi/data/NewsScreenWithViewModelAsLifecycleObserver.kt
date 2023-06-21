package com.compose.sample.mvi.data

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalLifecycleOwner
import com.compose.sample.mvi.NewsViewModelLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.compose.sample.mvi.ext.observeLifecycleEvents

@Composable
fun NewsScreenWithViewModelAsLifecycleObserver(
    viewModel:NewsViewModelLifecycle = viewModel()
) {
    val lifecycle = LocalLifecycleOwner.current.lifecycle
    viewModel.observeLifecycleEvents(lifecycle = lifecycle)
}