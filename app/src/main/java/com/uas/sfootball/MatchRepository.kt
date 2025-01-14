package com.uas.sfootball

import com.uas.sfootball.database.DateDao
import com.uas.sfootball.database.MatchDao
import com.uas.sfootball.models.Month

class MatchRepository(private val matchDao: MatchDao, private val dateDao: DateDao) {

    fun getMatches(dateId: Int) = matchDao.getMatches(dateId)

    fun getMatchesByDate(day: String, month: Month, year: String) = matchDao.getMatchesByDate(day, month, year)

    fun getDatesWithMatches() = dateDao.getDatesWithMatches()
}