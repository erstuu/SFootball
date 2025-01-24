package com.uas.sfootball.database

import androidx.room.Dao
import androidx.room.Delete
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

    @Query("""
        UPDATE dates 
        SET day = :day, 
            month = :month, 
            year = :year, 
            hour = :hour, 
            minute = :minute, 
            isClicked = :isClicked 
        WHERE id = :id
    """)
    suspend fun updateDateById(
        id: Int,
        day: String,
        month: Month,
        year: String,
        hour: String,
        minute: String,
        isClicked: Boolean
    )

    @Query("""
    UPDATE matches 
    SET nameHomeTeam = :nameHomeTeam, 
        nameAwayTeam = :nameAwayTeam, 
        logoHomeTeam = :logoHomeTeam, 
        logoAwayTeam = :logoAwayTeam, 
        stadium = :stadium, 
        score = :score 
    WHERE id = :id
""")
    suspend fun updateMatchById(
        id: Int,
        nameHomeTeam: String,
        nameAwayTeam: String,
        logoHomeTeam: String,
        logoAwayTeam: String,
        stadium: String,
        score: String? = null
    )

    @Query("SELECT * FROM dates WHERE id = :id")
    suspend fun getDateById(id: Int): Dates?

    @Query("SELECT * FROM matches WHERE dateId = :dateId")
    suspend fun getMatchesByDateId(dateId: Int): List<Match>

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

    @Transaction
    suspend fun updateMatchesAndDateById(id: Int, matchesWithDate: MatchesWithDate) {

        updateDateById(
            id = id,
            day = matchesWithDate.date.day,
            month = matchesWithDate.date.month,
            year = matchesWithDate.date.year,
            hour = matchesWithDate.date.hour,
            minute = matchesWithDate.date.minute,
            isClicked = matchesWithDate.date.isClicked
        )

        updateMatchById(
            id = id,
            nameHomeTeam = matchesWithDate.matches[0].nameHomeTeam,
            nameAwayTeam = matchesWithDate.matches[0].nameAwayTeam,
            logoHomeTeam = matchesWithDate.matches[0].logoHomeTeam,
            logoAwayTeam = matchesWithDate.matches[0].logoAwayTeam,
            stadium = matchesWithDate.matches[0].stadium,
            score = matchesWithDate.matches[0].score
        )
    }

    @Delete
    suspend fun deleteMatch(match: Match)
}