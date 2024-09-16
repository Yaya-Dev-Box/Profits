package com.yayarh.profits

import com.yayarh.profits.data.models.ProductEntity
import com.yayarh.profits.domain.models.CartItem
import com.yayarh.profits.domain.models.CombinedOrderItem
import com.yayarh.profits.domain.utils.DataGenerators
import org.junit.Assert.assertEquals
import org.junit.Test
import java.time.LocalDate

class DataGeneratorTest {

    private val ordersList = listOf(
        CombinedOrderItem(
            id = 1,
            products = listOf(
                CartItem(ProductEntity(1, "Burger", 75, 100), 1),
                CartItem(ProductEntity(2, "Pizza", 150, 200), 1),
                CartItem(ProductEntity(3, "Pasta", 50, 70), 1),
            )
        ),
        CombinedOrderItem(
            id = 2,
            products = listOf(
                CartItem(ProductEntity(4, "Fries", 25, 30), 3),
                CartItem(ProductEntity(2, "Pizza", 150, 200), 1),
                CartItem(ProductEntity(3, "Pasta", 50, 70), 1),
                CartItem(ProductEntity(3, "Pasta", 50, 70), 5),
            )
        ),
        CombinedOrderItem(
            id = 3,
            products = listOf(
                CartItem(ProductEntity(5, "Coke", 10, 15), 1),
                CartItem(ProductEntity(4, "Fries", 25, 30), 1),
                CartItem(ProductEntity(4, "Fries", 25, 30), 2),
                CartItem(ProductEntity(4, "Fries", 25, 30), 1),
            )
        ),
    )

    @Test
    fun testSalesListGeneration() {
        val entity = DataGenerators.generateDailySaleEntity(ordersList, LocalDate.now()) ?: return

        assert(entity.salesList.contains("Burger x1"))
        assert(entity.salesList.contains("Pizza x2"))
        assert(entity.salesList.contains("Pasta x7"))
        assert(entity.salesList.contains("Fries x7"))
        assert(entity.salesList.contains("Coke x1"))
    }

    @Test
    fun testSalesAndProfitsCalculation() {
        val entity = DataGenerators.generateDailySaleEntity(ordersList, LocalDate.now()) ?: return

        assertEquals(1215, entity.totalSales)
        assertEquals(305, entity.profits)
    }

}