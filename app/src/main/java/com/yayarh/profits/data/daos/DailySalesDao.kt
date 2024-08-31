package com.yayarh.profits.data.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.yayarh.profits.data.models.DailySalesEntity
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

@Dao
interface DailySalesDao {

    @Query("SELECT * FROM DailySales WHERE date BETWEEN :startDate AND :endDate ORDER BY date DESC")
    fun getDailySalesItems(startDate: LocalDate, endDate: LocalDate): Flow<List<DailySalesEntity>>

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertDailySalesItem(dailySales: DailySalesEntity)

    @Query("DELETE FROM DailySales WHERE id = :id")
    suspend fun deleteDailySalesItem(id: Int)

}