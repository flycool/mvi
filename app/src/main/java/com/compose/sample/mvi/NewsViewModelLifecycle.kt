package com.compose.sample.mvi

import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class NewsViewModelLifecycle : ViewModel(), DefaultLifecycleObserver {
    override fun onResume(owner: LifecycleOwner) {
        viewModelScope.launch {
            println("viewModel onResume observe")
        }
    }
}