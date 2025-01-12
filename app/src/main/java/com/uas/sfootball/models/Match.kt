package com.uas.sfootball.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "matches")
data class Match(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val nameHomeTeam: String,
    val nameAwayTeam: String,
    val logoHomeTeam: Int,
    val logoAwayTeam: Int,
    val dateId: Int,
    val score: String? = null
)