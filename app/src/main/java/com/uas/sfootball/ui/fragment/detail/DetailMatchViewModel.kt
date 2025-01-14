package com.uas.sfootball.ui.fragment.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.uas.sfootball.MatchRepository

class DetailMatchViewModel(private val repository: MatchRepository) : ViewModel() {
    fun getMatchById(id: Int) = repository.getMatchById(id).asLiveData()
}