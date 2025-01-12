package com.uas.sfootball

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.uas.sfootball.ui.HomeViewModel

class ViewModelFactory(private val repository: MatchRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return HomeViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}