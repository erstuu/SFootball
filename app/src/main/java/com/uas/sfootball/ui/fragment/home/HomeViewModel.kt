package com.uas.sfootball.ui.fragment.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.uas.sfootball.MatchRepository
import com.uas.sfootball.helper.DatePickerHelper
import com.uas.sfootball.models.MatchesWithDate
import com.uas.sfootball.models.Month
import kotlinx.coroutines.launch
import java.util.Calendar

class HomeViewModel(private val repository: MatchRepository) : ViewModel() {

    init {
        getMatchSchedule()
    }

    private val _matchesWithDate = MutableLiveData<MutableList<MatchesWithDate>>()
    val matchesWithDate: LiveData<MutableList<MatchesWithDate>> get() = _matchesWithDate

    fun getMatchById(id: Int): LiveData<MatchesWithDate> {
        return repository.getMatchById(id).asLiveData()
    }

    fun updateMatchesAndDateById(id: Int, matchesWithDate: MatchesWithDate) {
        viewModelScope.launch {
            repository.updateMatchesAndDateById(id, matchesWithDate)
        }
    }

    fun deleteMatch(match: MatchesWithDate) {
        viewModelScope.launch {
            repository.deleteMatch(match.matches[0])
            getMatchSchedule()
        }
    }

    private fun getMatchSchedule() {
        val dateNow = Calendar.getInstance()
        val dayNow = dateNow.get(Calendar.DAY_OF_MONTH)
        val monthNow = dateNow.get(Calendar.MONTH) + 1
        val yearNow = dateNow.get(Calendar.YEAR)

        val day = dayNow.toString()
        val month = DatePickerHelper.getMonthByNum(monthNow)
        val year = yearNow.toString()

        getMatchesByDate(day, month, year).observeForever {
            _matchesWithDate.value = it
        }
    }

    fun getMatchesByDate(
        day: String,
        month: Month,
        year: String
    ): LiveData<MutableList<MatchesWithDate>> {
        return repository.getMatchesByDate(day, month, year).asLiveData()
    }

}