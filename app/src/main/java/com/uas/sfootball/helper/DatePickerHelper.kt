package com.uas.sfootball.helper

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
            0 -> "Januari"
            1 -> "Februari"
            2 -> "Maret"
            3 -> "April"
            4 -> "Mei"
            5 -> "Juni"
            6 -> "Juli"
            7 -> "Agustus"
            8 -> "September"
            9 -> "Oktober"
            10 -> "November"
            11 -> "Desember"
            else -> "Unknown"
        }
    }

    fun getMonthByNum(month: Int): Month {
        return when (month) {
            0 -> Month.Januari
            1 -> Month.Februari
            2 -> Month.Maret
            3 -> Month.April
            4 -> Month.Mei
            5 -> Month.Juni
            6 -> Month.Juli
            7 -> Month.Agustus
            8 -> Month.September
            9 -> Month.Oktober
            10 -> Month.November
            11 -> Month.December
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
}