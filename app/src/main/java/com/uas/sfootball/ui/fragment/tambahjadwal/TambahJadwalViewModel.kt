package com.uas.sfootball.ui.fragment.tambahjadwal

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.uas.sfootball.MatchRepository
import com.uas.sfootball.models.MatchesWithDate
import kotlinx.coroutines.launch

class TambahJadwalViewModel(private val repository: MatchRepository) : ViewModel() {
    fun addMatch(match: MatchesWithDate) {
        viewModelScope.launch {
            repository.addMatch(match)
        }
    }
}