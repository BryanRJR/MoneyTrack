package com.bryanrantung.moneytrack.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.bryanrantung.moneytrack.model.Transaction

@Database(entities = [Transaction::class], version = 1)
abstract class MoneyTrackDatabase: RoomDatabase() {
    abstract fun cashFlowDao(): MoneyTrackDao

    companion object {
        @Volatile
        private var INSTANCE: MoneyTrackDatabase? = null

        fun getInstance(context: Context): MoneyTrackDatabase {
            synchronized(this) {
                var instance = INSTANCE
                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        MoneyTrackDatabase::class.java,
                        "cash_flow_database"
                    ).fallbackToDestructiveMigration().build()
                    INSTANCE = instance
                }

                return instance
            }
        }
    }
}