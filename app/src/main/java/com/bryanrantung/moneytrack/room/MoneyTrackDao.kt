package com.bryanrantung.moneytrack.room

import androidx.lifecycle.LiveData
import androidx.room.*
import com.bryanrantung.moneytrack.model.Transaction
import kotlinx.coroutines.flow.Flow

@Dao
interface MoneyTrackDao {

        @Insert
        suspend fun insert(cashFlowEntity: Transaction)

        @Update
        suspend fun update(cashFlowEntity: Transaction)

        @Delete
        suspend fun delete(cashFlowEntity: Transaction)

        @Query("SELECT * FROM `cash-flow-table`")
        // flow is used to hold a value that can change at runtime
        fun fetchAllCashFlow(): Flow<List<Transaction>>

        @Query("SELECT * FROM `cash-flow-table` WHERE id=:id")
        fun fetchCashFlowById(id: Int): Flow<Transaction>

        @Query("SELECT * FROM `cash-flow-table` WHERE type=:type")
        fun fetchAllIncome(type: String): Flow<List<Transaction>>

        @Query("SELECT * FROM `cash-flow-table` WHERE type=:type")
        fun fetchAllOutcome(type: String): Flow<List<Transaction>>

        @Query("SELECT SUM(amount) as total FROM `cash-flow-table` WHERE type=:type")
        suspend fun calculateIncome(type: String): Int?

        @Query("SELECT SUM(amount) as total FROM `cash-flow-table` WHERE type=:type")
        suspend fun calculateOutcome(type: String): Int?

        @Query("SELECT * FROM `cash-flow-table` LIMIT 3")
        fun fetchRecentTransaction(): Flow<List<Transaction>>


}