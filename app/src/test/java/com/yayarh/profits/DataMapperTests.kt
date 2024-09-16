package com.yayarh.profits

import com.yayarh.profits.data.models.OrderEntity
import com.yayarh.profits.data.models.ProductEntity
import com.yayarh.profits.domain.models.CombinedOrderItem
import com.yayarh.profits.domain.utils.DataMappers.toCombinedOrderItem
import org.junit.Assert.assertEquals
import org.junit.Test

class DataMapperTests {

    private val productsList = listOf(
        ProductEntity(1, "Burger", 75, 100),
        ProductEntity(2, "Pizza", 150, 200),
        ProductEntity(3, "Pasta", 50, 70),
        ProductEntity(4, "Fries", 25, 30),
        ProductEntity(5, "Coke", 10, 15)
    )

    @Test
    fun orderMapping() {
        val ordersList = listOf(
            OrderEntity(1, 1, 1, productsList[0]),
            OrderEntity(7, 4, 1, productsList[2]),
            OrderEntity(3, 2, 1, productsList[3]),
            OrderEntity(5, 3, 1, productsList[4]),

            OrderEntity(2, 1, 2, productsList[1]),

            OrderEntity(4, 2, 3, productsList[2]),

            OrderEntity(6, 3, 4, productsList[3]),
            OrderEntity(9, 2, 4, productsList[3]),

            OrderEntity(8, 4, 5, productsList[4])
        )

        val combinedOrders: List<CombinedOrderItem> = ordersList.toCombinedOrderItem()

        assertEquals(5, combinedOrders.size)
        assertEquals(4, combinedOrders.first { it.id == 1 }.products.size)
        assertEquals(1, combinedOrders.first { it.id == 2 }.products.size)
        assertEquals(1, combinedOrders.first { it.id == 3 }.products.size)

        assertEquals(2, combinedOrders.first { it.id == 4 }.products.size)

        assertEquals(1, combinedOrders.first { it.id == 5 }.products.size)
    }

}