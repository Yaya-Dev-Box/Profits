package com.yayarh.profits.data.repos.imps

import com.yayarh.profits.data.daos.OrdersDao
import com.yayarh.profits.data.models.OrderEntity
import com.yayarh.profits.data.repos.base.OrdersRepo
import com.yayarh.profits.data.utils.safeCall
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class OrdersRepoImpl @Inject constructor(private val ordersDao: OrdersDao) : OrdersRepo {

    override suspend fun getAllOrders(): Flow<List<OrderEntity>> = ordersDao.getAllOrders()

    override suspend fun insertOrder(order: OrderEntity) = safeCall { ordersDao.insertOrder(order) }

    override suspend fun insertOrders(orders: List<OrderEntity>) = safeCall { ordersDao.insertOrders(orders) }

    override suspend fun deleteOrderByOrderId(orderId: Int) = safeCall { ordersDao.deleteOrdersByOrderId(orderId) }

    override suspend fun deleteAllOrders() = safeCall { ordersDao.deleteAllOrders() }

}