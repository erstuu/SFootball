package com.uas.sfootball.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import com.uas.sfootball.models.Dates
import com.uas.sfootball.models.Match
import com.uas.sfootball.models.MatchesWithDate
import com.uas.sfootball.models.Month
import kotlinx.coroutines.flow.Flow

@Dao
interface MatchDao {

    @Insert
    fun insertMatches(matches: List<Match>)

    @Insert
    suspend fun insertDate(date: Dates): Long

    @Insert
    suspend fun insertMatch(match: Match)

    @Transaction
    suspend fun insertMatchWithDate(matchesWithDate: MatchesWithDate) {
        val dateId = insertDate(matchesWithDate.date)

        val dateIdInt = dateId.toInt()

        matchesWithDate.matches.forEach { match ->
            insertMatch(match.copy(dateId = dateIdInt))
        }
    }

    @Query("SELECT * FROM matches WHERE dateId = :dateId")
    fun getMatches(dateId: Int): Flow<List<Match>>

    @Query("""
        SELECT matches.*, dates.day, dates.month, dates.year, dates.hour, dates.minute, dates.isClicked 
        FROM matches
        INNER JOIN dates ON matches.dateId = dates.id
        WHERE dates.day = :day AND dates.month = :month AND dates.year = :year
    """)
    fun getMatchesByDate(day: String, month: Month, year: String): Flow<MutableList<MatchesWithDate>>

    @Query("""
    SELECT matches.*, dates.day, dates.month, dates.year, dates.hour, dates.minute, dates.isClicked 
    FROM matches 
    INNER JOIN dates ON matches.dateId = dates.id 
    WHERE matches.id = :id
    """)
    fun getMatchById(id: Int): Flow<MatchesWithDate>
}