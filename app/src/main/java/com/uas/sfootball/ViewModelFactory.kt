package com.uas.sfootball

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.uas.sfootball.ui.fragment.detail.DetailMatchViewModel
import com.uas.sfootball.ui.fragment.home.HomeViewModel
import com.uas.sfootball.ui.fragment.tambahjadwal.TambahJadwalViewModel

class ViewModelFactory(private val repository: MatchRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return HomeViewModel(repository) as T
        }
        if (modelClass.isAssignableFrom(TambahJadwalViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return TambahJadwalViewModel(repository) as T
        }
        if(modelClass.isAssignableFrom(DetailMatchViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return DetailMatchViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}