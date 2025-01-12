package com.uas.sfootball.models

import androidx.room.Embedded
import androidx.room.Relation

data class DatesWithMatches(
    @Embedded val date: Dates,
    @Relation(
        parentColumn = "id",
        entityColumn = "dateId"
    )
    val matches: List<Match>
)