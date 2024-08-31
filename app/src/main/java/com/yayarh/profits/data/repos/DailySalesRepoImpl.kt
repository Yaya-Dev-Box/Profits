package com.yayarh.profits.data.repos

import com.yayarh.profits.data.daos.DailySalesDao
import com.yayarh.profits.data.models.DailySalesEntity
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate
import javax.inject.Inject

class DailySalesRepoImpl @Inject constructor(private val salesDao: DailySalesDao) : DailySalesRepo {

    override fun getDailySalesItems(startDate: LocalDate, endDate: LocalDate): Flow<List<DailySalesEntity>> {
        return salesDao.getDailySalesItems(startDate, endDate)
    }

    override suspend fun insertDailySalesItem(dailySales: DailySalesEntity) = salesDao.insertDailySalesItem(dailySales)

    override suspend fun deleteDailySalesItem(id: Int) = salesDao.deleteDailySalesItem(id)

}