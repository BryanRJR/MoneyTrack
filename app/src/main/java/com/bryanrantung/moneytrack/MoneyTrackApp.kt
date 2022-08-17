package com.bryanrantung.moneytrack

import android.app.Application
import com.bryanrantung.moneytrack.room.MoneyTrackDatabase

class MoneyTrackApp: Application() {
    val db by lazy {
        MoneyTrackDatabase.getInstance(this)
    }
}