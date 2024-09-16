package com.yayarh.profits.data.repos.mocks

import com.yayarh.profits.data.models.OrderEntity
import com.yayarh.profits.data.models.ProductEntity
import com.yayarh.profits.data.repos.base.OrdersRepo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

open class FakeOrdersRepo : OrdersRepo {

    private val fakeOrdersList = listOf(
        OrderEntity(1, 5, 1, ProductEntity(1, "Burger", 75, 100)),
        OrderEntity(1, 2, 1, ProductEntity(1, "Potato Salad", 50, 15)),
        OrderEntity(1, 1, 1, ProductEntity(1, "Polo", 70, 125)),

        OrderEntity(2, 3, 2, ProductEntity(2, "Pizza", 50, 75)),
        OrderEntity(2, 4, 2, ProductEntity(2, "Pasta", 40, 60)),
        OrderEntity(2, 5, 2, ProductEntity(2, "Salad", 30, 45)),
        OrderEntity(2, 1, 2, ProductEntity(2, "Burger", 75, 100)),
        OrderEntity(2, 2, 2, ProductEntity(2, "Potato Salad", 50, 15)),

        OrderEntity(3, 2, 3, ProductEntity(3, "Ifruit", 10, 20)),
        OrderEntity(3, 3, 3, ProductEntity(3, "Hamoud", 10, 20)),

        OrderEntity(4, 1, 4, ProductEntity(4, "Fries", 20, 30)),
    )

    override suspend fun getAllOrders(): Flow<List<OrderEntity>> = flowOf(fakeOrdersList)

    override suspend fun insertOrder(order: OrderEntity): Result<Unit> = Result.success(Unit)

    override suspend fun insertOrders(orders: List<OrderEntity>): Result<Unit> = Result.success(Unit)

    override suspend fun deleteOrderByOrderId(orderId: Int): Result<Unit> = Result.success(Unit)

    override suspend fun deleteAllOrders(): Result<Unit> = Result.success(Unit)

}