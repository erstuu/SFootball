package com.uas.sfootball.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.uas.sfootball.MatchRepository
import com.uas.sfootball.models.Month

class HomeViewModel(private val repository: MatchRepository) : ViewModel() {
    fun getMatches(dateId: Int) = repository.getMatches(dateId).asLiveData()

    fun getMatchesByDate(
        day: String,
        month: Month,
        year: String) = repository.getMatchesByDate(day, month, year).asLiveData()

    fun getDatesWithMatches() = repository.getDatesWithMatches().asLiveData()
}