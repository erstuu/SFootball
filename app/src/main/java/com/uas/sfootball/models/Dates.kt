package com.uas.sfootball.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "dates")
data class Dates(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    var day: String,
    val month: Month,
    var year: String,
    var hour: String,
    var minute: String,
    var isClicked: Boolean = false
)

enum class Month {
    Januari, Februari, Maret, April, Mei, Juni,
    Juli, Agustus, September, Oktober, November, December, Unknown
}

data class MDate(
    val days: List<String>,
    val dayNumbers: List<Int>,
    val month: Int,
    val year: String
)

data class DateInfo(
    val day: String,
    val dayNumber: Int
)

data class CurrentDate(
    val day: Int,
    val month: Month,
    val year: Int
)
