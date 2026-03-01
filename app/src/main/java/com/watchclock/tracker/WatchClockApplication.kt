package com.watchclock.tracker

import android.app.Application
import com.watchclock.tracker.data.database.AppDatabase

class WatchClockApplication : Application() {

    val database: AppDatabase by lazy {
        AppDatabase.getInstance(this)
    }
}
