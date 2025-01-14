package com.uas.sfootball.helper

import android.content.Context
import com.uas.sfootball.R
import com.uas.sfootball.models.Dates
import com.uas.sfootball.models.Match
import com.uas.sfootball.models.Month

object InitialDataSource {
    fun getDates(): List<Dates> {
        return listOf(
            Dates(1, "10", Month.Januari, "2025", "20", "00"),
            Dates(2, "10", Month.Januari, "2025", "22", "00"),
            Dates(3, "10", Month.Januari, "2025", "01", "00"),
            Dates(4, "14", Month.Januari, "2025", "20", "00"),
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
                1
            ),
            Match(
                2,
                "Te Fc",
                "Foc Fc",
                StorageHelper.getDrawableUri(context, R.drawable.te_fc).toString(),
                StorageHelper.getDrawableUri(context, R.drawable.lion_fc).toString(),
                2
            ),
            Match(
                3,
                "Tiger Fc",
                "Ft Fc",
                StorageHelper.getDrawableUri(context, R.drawable.tiger_fc).toString(),
                StorageHelper.getDrawableUri(context, R.drawable.ft_fc).toString(),
                3
            ),
            Match(
                4,
                "Tiger Fc",
                "Ft Fc",
                StorageHelper.getDrawableUri(context, R.drawable.tiger_fc).toString(),
                StorageHelper.getDrawableUri(context, R.drawable.ft_fc).toString(),
                4
            )
        )
    }
}