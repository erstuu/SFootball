package com.uas.sfootball.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import com.uas.sfootball.models.DatesWithMatches
import com.uas.sfootball.models.Match
import kotlinx.coroutines.flow.Flow

@Dao
interface MatchDao {

    @Insert
    fun insertMatches(matches: List<Match>)

    @Query("SELECT * FROM matches WHERE dateId = :dateId")
    fun getMatches(dateId: Int): Flow<List<Match>>

    @Transaction
    @Query("SELECT * FROM matches")
    fun getDatesWithMatches(): Flow<List<DatesWithMatches>>
}