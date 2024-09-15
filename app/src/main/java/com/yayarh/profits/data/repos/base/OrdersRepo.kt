package com.yayarh.profits.data.repos.base

import com.yayarh.profits.data.models.OrderEntity
import kotlinx.coroutines.flow.Flow

interface OrdersRepo {

    suspend fun getAllOrders(): Flow<List<OrderEntity>>

    suspend fun insertOrder(order: OrderEntity): Result<Unit>

    suspend fun insertOrders(orders: List<OrderEntity>): Result<Unit>

    suspend fun deleteOrder(id: Int): Result<Unit>

    suspend fun deleteAllOrders(): Result<Unit>
}