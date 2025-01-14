package com.uas.sfootball.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "matches")
data class Match(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val nameHomeTeam: String,
    val nameAwayTeam: String,
    val logoHomeTeam: String,
    val logoAwayTeam: String,
    val dateId: Int,
    val score: String? = null
)