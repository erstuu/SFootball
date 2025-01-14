package com.uas.sfootball.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import com.uas.sfootball.models.Dates
import com.uas.sfootball.models.MatchesWithDate
import kotlinx.coroutines.flow.Flow

@Dao
interface DateDao {

    @Insert
    suspend fun insertDates(date: List<Dates>)

    @Query("SELECT * FROM dates WHERE day = :day AND month = :month AND year = :year")
    suspend fun getDate(day: String, month: String, year: String): Dates

    @Transaction
    @Query("SELECT * FROM dates")
    fun getDatesWithMatches(): Flow<MutableList<MatchesWithDate>>
}