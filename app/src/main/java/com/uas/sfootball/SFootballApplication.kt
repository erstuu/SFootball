package com.uas.sfootball

import android.app.Application
import com.uas.sfootball.database.SFootballDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

class SFootballApplication : Application() {
    private val applicationScope = CoroutineScope(SupervisorJob())
    val database by lazy { SFootballDatabase.getDatabase(this, applicationScope) }
    val repository by lazy { MatchRepository(database.matchDao(), database.dateDao()) }
}