package com.uas.sfootball

import com.uas.sfootball.database.DateDao
import com.uas.sfootball.database.MatchDao
import com.uas.sfootball.models.Match
import com.uas.sfootball.models.MatchesWithDate
import com.uas.sfootball.models.Month

class MatchRepository(private val matchDao: MatchDao, private val dateDao: DateDao) {

    fun getMatches(dateId: Int) = matchDao.getMatches(dateId)

    fun getMatchById(id: Int) = matchDao.getMatchById(id)

    fun getMatchesByDate(day: String, month: Month, year: String) = matchDao.getMatchesByDate(day, month, year)

    suspend fun addMatch(match: MatchesWithDate) {
        matchDao.insertMatchWithDate(match)
    }

    suspend fun updateMatchesAndDateById(id: Int, matchesWithDate: MatchesWithDate) = matchDao.updateMatchesAndDateById(id, matchesWithDate)

    suspend fun deleteMatch(match: Match) = matchDao.deleteMatch(match)
}