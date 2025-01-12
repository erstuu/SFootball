package com.uas.sfootball.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.uas.sfootball.models.Dates
import kotlinx.coroutines.flow.Flow

@Dao
interface DateDao {

    @Insert
    suspend fun insertDates(date: List<Dates>)

    @Query("SELECT * FROM dates")
    fun getDates(): Flow<List<Dates>>
}