package com.yayarh.profits.domain.models

data class CombinedOrderItem(val id: Int, val products: List<CartItem>) {

    fun generateSaleSummary() = products.joinToString("") { it.product.name + " x" + it.quantity + "\n" }.trim()

    fun getProfits() = products.sumOf { it.product.getProfit() * it.quantity }
}