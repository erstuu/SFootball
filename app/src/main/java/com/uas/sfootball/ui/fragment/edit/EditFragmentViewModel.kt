package com.uas.sfootball.ui.fragment.edit

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.uas.sfootball.MatchRepository
import com.uas.sfootball.models.MatchesWithDate
import kotlinx.coroutines.launch

class EditFragmentViewModel(private val repository: MatchRepository) : ViewModel() {
    fun getMatchById(id: Int): LiveData<MatchesWithDate> {
        return repository.getMatchById(id).asLiveData()
    }

    fun updateMatchesAndDateById(id: Int, matchesWithDate: MatchesWithDate) {
        viewModelScope.launch {
            repository.updateMatchesAndDateById(id, matchesWithDate)
        }
    }
}