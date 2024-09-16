package com.yayarh.profits.domain.utils

import com.yayarh.profits.data.models.DailySalesEntity
import com.yayarh.profits.data.models.ProductEntity
import com.yayarh.profits.domain.models.CombinedOrderItem
import java.time.LocalDate

object DataGenerators {

    fun generateDailySaleEntity(ordersList: List<CombinedOrderItem>, date: LocalDate): DailySalesEntity? {
        if (ordersList.isEmpty()) return null

        val fullCartList = ordersList.map { it.products }.flatten()

        val productsQuantities: Map<ProductEntity, Int> = buildMap {
            fullCartList.forEach {
                val quantityInMap = this.getOrDefault(it.product, 0)
                put(it.product, quantityInMap + it.quantity)
            }
        }

        val saleSummary = productsQuantities.entries.joinToString("") { "${it.key.name} x${it.value} \n" }.trim()

        val totalSales = fullCartList.sumOf { it.product.sellPrice * it.quantity }
        val totalProfits = fullCartList.sumOf { (it.product.sellPrice - it.product.buyPrice) * it.quantity }

        return DailySalesEntity(0, date, saleSummary, totalSales, totalProfits)
    }

}