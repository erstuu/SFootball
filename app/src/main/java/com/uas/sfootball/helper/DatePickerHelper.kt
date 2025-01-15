package com.uas.sfootball.helper

import com.uas.sfootball.models.CurrentDate
import com.uas.sfootball.models.DateInfo
import com.uas.sfootball.models.Month
import java.util.Calendar

object DatePickerHelper {
    fun getDayName(year: Int, month: Int, day: Int): String {
        return Calendar.getInstance().apply {
            set(year, month, day)
        }.let { calendar ->
            when (calendar.get(Calendar.DAY_OF_WEEK)) {
                Calendar.MONDAY -> "Senin"
                Calendar.TUESDAY -> "Selasa"
                Calendar.WEDNESDAY -> "Rabu"
                Calendar.THURSDAY -> "Kamis"
                Calendar.FRIDAY -> "Jumat"
                Calendar.SATURDAY -> "Sabtu"
                Calendar.SUNDAY -> "Minggu"
                else -> ""
            }
        }
    }

    fun getMonthName(month: Int): String {
        return when (month) {
            1 -> "Januari"
            2 -> "Februari"
            3 -> "Maret"
            4 -> "April"
            5 -> "Mei"
            6 -> "Juni"
            7 -> "Juli"
            8 -> "Agustus"
            9 -> "September"
            10 -> "Oktober"
            11 -> "November"
            12 -> "Desember"
            else -> "Unknown"
        }
    }

    fun getMonthByNum(month: Int): Month {
        return when (month) {
            1 -> Month.Januari
            2 -> Month.Februari
            3 -> Month.Maret
            4 -> Month.April
            5 -> Month.Mei
            6 -> Month.Juni
            7 -> Month.Juli
            8 -> Month.Agustus
            9 -> Month.September
            10 -> Month.Oktober
            11 -> Month.November
            12 -> Month.December
            else -> Month.Unknown
        }
    }

    fun getAllDatesInMonth(year: Int, month: Int): List<DateInfo> {
        val calendar = Calendar.getInstance()
        calendar.set(year, month, 1)

        val dates = mutableListOf<DateInfo>()

        while (calendar.get(Calendar.MONTH) == month) {
            val dayNumber = calendar.get(Calendar.DAY_OF_MONTH)
            val dayName = getDayName(
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            )

            dates.add(
                DateInfo(
                    day = dayName,
                    dayNumber = dayNumber
                )
            )

            calendar.add(Calendar.DAY_OF_MONTH, 1)
        }

        return dates
    }

    fun getNumberOfMonth(month: Month): Int {
        return when (month) {
            Month.Januari -> 1
            Month.Februari -> 2
            Month.Maret -> 3
            Month.April -> 4
            Month.Mei -> 5
            Month.Juni -> 6
            Month.Juli -> 7
            Month.Agustus -> 8
            Month.September -> 9
            Month.Oktober -> 10
            Month.November -> 11
            Month.December -> 12
            Month.Unknown -> -1
        }
    }

    fun getCurrentDate(): CurrentDate {
        val calendar = Calendar.getInstance()
        val dayNumber = calendar.get(Calendar.DAY_OF_MONTH)
        val month = calendar.get(Calendar.MONTH) + 1
        val year = calendar.get(Calendar.YEAR)

        return CurrentDate(
            day = dayNumber,
            month = getMonthByNum(month),
            year = year
        )
    }
}