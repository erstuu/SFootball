package com.uas.sfootball.helper

import android.content.Context
import com.uas.sfootball.R
import com.uas.sfootball.models.Dates
import com.uas.sfootball.models.Match

object InitialDataSource {
    fun getDates(): List<Dates> {
        val currentDate = DatePickerHelper.getCurrentDate()
        val day = currentDate.day.toString()
        val month = currentDate.month
        val year = currentDate.year.toString()
        return listOf(
            Dates(1, day, month, year, "20", "00"),
            Dates(2, day, month, year, "22", "00"),
            Dates(3, day, month, year, "01", "00"),
            Dates(4, day, month, year, "03", "00"),
        )
    }

    fun getMatches(context: Context): List<Match> {
        return listOf(
            Match(
                1,
                "Lion Fc",
                "Foo Fc",
                StorageHelper.getDrawableUri(context, R.drawable.lion_fc).toString(),
                StorageHelper.getDrawableUri(context, R.drawable.foo_fc).toString(),
                1,
                stadium = "Stadion Dua"
            ),
            Match(
                2,
                "Te Fc",
                "Foc Fc",
                StorageHelper.getDrawableUri(context, R.drawable.te_fc).toString(),
                StorageHelper.getDrawableUri(context, R.drawable.lion_fc).toString(),
                2,
                stadium = "Stadion Utama"
            ),
            Match(
                3,
                "Tiger Fc",
                "Ft Fc",
                StorageHelper.getDrawableUri(context, R.drawable.tiger_fc).toString(),
                StorageHelper.getDrawableUri(context, R.drawable.ft_fc).toString(),
                3,
                stadium = "Stadion Tiga"
            ),
            Match(
                4,
                "Te Fc",
                "Lion Fc",
                StorageHelper.getDrawableUri(context, R.drawable.te_fc).toString(),
                StorageHelper.getDrawableUri(context, R.drawable.lion_fc).toString(),
                4,
                "2-1",
                "Stadion Utama"
            )
        )
    }
}