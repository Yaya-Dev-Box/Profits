package com.yayarh.profits.data.repos.mocks

import com.yayarh.profits.data.models.DailySalesEntity
import com.yayarh.profits.data.repos.DailySalesRepo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import java.time.LocalDate

open class FakeSalesRepo : DailySalesRepo {

    private val fakeSalesList = listOf(
        DailySalesEntity(1, LocalDate.now(), "Burger x 10", 100,50),
        DailySalesEntity(2, LocalDate.now().plusDays(1), "Pizza x 5", 50,25),
        DailySalesEntity(3, LocalDate.now().plusDays(2), "Coke x 2", 20,10),
        DailySalesEntity(4, LocalDate.now().plusDays(3), "Fries x 3", 30,15),
    )

    override fun getDailySalesItems(startDate: LocalDate, endDate: LocalDate): Flow<List<DailySalesEntity>> = flowOf(fakeSalesList)

    override suspend fun insertDailySalesItem(dailySales: DailySalesEntity) = Unit

    override suspend fun deleteDailySalesItem(id: Int) = Unit

}