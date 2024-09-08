package com.yayarh.profits.data.repos

import com.yayarh.profits.data.models.DailySalesEntity
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

interface DailySalesRepo {

    suspend fun getDailySalesItems(startDate: LocalDate, endDate: LocalDate): Flow<List<DailySalesEntity>>

    suspend fun insertDailySalesItem(dailySales: DailySalesEntity)

    suspend fun deleteDailySalesItem(id: Int)

}