package com.uas.sfootball.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.uas.sfootball.MatchRepository

class HomeViewModel(private val repository: MatchRepository) : ViewModel() {
    fun getMatches(dateId: Int) = repository.getMatches(dateId).asLiveData()
    fun getDatesWithMatches() = repository.getDatesWithMatches().asLiveData()
}