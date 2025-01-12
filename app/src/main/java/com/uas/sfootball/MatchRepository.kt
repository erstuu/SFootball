package com.uas.sfootball

import com.uas.sfootball.database.DateDao
import com.uas.sfootball.database.MatchDao

class MatchRepository(private val matchDao: MatchDao, private val dateDao: DateDao) {

    fun getDates() = dateDao.getDates()

    fun getMatches(dateId: Int) = matchDao.getMatches(dateId)

    fun getDatesWithMatches() = matchDao.getDatesWithMatches()
}