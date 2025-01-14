package com.uas.sfootball.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.uas.sfootball.helper.InitialDataSource
import com.uas.sfootball.models.Dates
import com.uas.sfootball.models.Match
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Database(
    entities = [Match::class, Dates::class],
    version = 1,
    exportSchema = false
)
abstract class SFootballDatabase : RoomDatabase() {
    abstract fun matchDao(): MatchDao
    abstract fun dateDao(): DateDao

    companion object {
        @Volatile
        private var INSTANCE: SFootballDatabase? = null

        fun getDatabase(context: Context, applicationScope: CoroutineScope): SFootballDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    SFootballDatabase::class.java,
                    "sfootball_database"
                )
                    .fallbackToDestructiveMigration()
                    .addCallback(object : Callback() {
                        override fun onCreate(db: SupportSQLiteDatabase) {
                            super.onCreate(db)
                            INSTANCE?.let { database ->
                                applicationScope.launch {
                                    val dateDao = database.dateDao()
                                    dateDao.insertDates(InitialDataSource.getDates())
                                    val matchDao = database.matchDao()
                                    matchDao.insertMatches(InitialDataSource.getMatches(context))
                                }
                            }
                        }
                    })
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}