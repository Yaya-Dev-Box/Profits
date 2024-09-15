package com.yayarh.profits.data.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.yayarh.profits.data.models.OrderEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface OrdersDao {

    @Query("SELECT * FROM Orders")
    fun getAllOrders(): Flow<List<OrderEntity>>

    @Query("SELECT * FROM Orders WHERE orderId = :orderId")
    fun getOrdersByOrderId(orderId: Int): Flow<List<OrderEntity>>

    @Query("SELECT * FROM Orders WHERE embeddedProduct_id = :productId")
    fun getOrdersByProductId(productId: Int): Flow<List<OrderEntity>>


    @Insert
    suspend fun insertOrder(order: OrderEntity)

    @Insert
    suspend fun insertOrders(orders: List<OrderEntity>)


    @Query("DELETE FROM Orders WHERE orderId = :orderId")
    suspend fun deleteOrdersByOrderId(orderId: Int)

    @Query("DELETE FROM Orders")
    suspend fun deleteAllOrders()

}