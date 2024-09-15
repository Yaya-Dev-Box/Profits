package com.yayarh.profits.domain.utils

import com.yayarh.profits.data.models.OrderEntity
import com.yayarh.profits.domain.models.CartItem
import com.yayarh.profits.domain.models.CombinedOrderItem

object DataMappers {

    fun OrderEntity.toCartItem() = CartItem(quantity = quantity, product = product)

    fun List<OrderEntity>.toCombinedOrderItem(): List<CombinedOrderItem> {
        val combinedOrderItems = mutableListOf<CombinedOrderItem>()
        this.groupBy { it.orderId }.forEach { (orderId, orderProducts) ->
            combinedOrderItems.add(CombinedOrderItem(orderId, orderProducts.map { it.toCartItem() }))
        }
        return combinedOrderItems
    }

}