package com.uas.sfootball.helper

import com.uas.sfootball.R
import com.uas.sfootball.models.Dates
import com.uas.sfootball.models.Match
import com.uas.sfootball.models.Month

object InitialDataSource {
    fun getDates(): List<Dates> {
        return listOf(
            Dates(1, "Rabu", Month.Januari, "2025", "20", "00"),
            Dates(2, "Kamis", Month.Januari, "2025", "22", "00"),
            Dates(3, "Jumat", Month.Januari, "2025", "01", "00"),
        )
    }

    fun getMatches(): List<Match> {
        return listOf(
            Match(1, "Lion Fc", "Foo Fc", R.drawable.lion_fc, R.drawable.foo_fc, 1),
            Match(2, "Te Fc", "Foc Fc", R.drawable.te_fc, R.drawable.lion_fc, 2),
            Match(3, "Tiger Fc", "Ft Fc", R.drawable.tiger_fc, R.drawable.ft_fc, 3)
        )
    }
}