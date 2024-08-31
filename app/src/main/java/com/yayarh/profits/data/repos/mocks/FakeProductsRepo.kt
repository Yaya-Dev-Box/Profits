package com.yayarh.profits.data.repos.mocks

import com.yayarh.profits.data.models.ProductEntity
import com.yayarh.profits.data.repos.ProductsRepo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

open class FakeProductsRepo : ProductsRepo {

    val productsList = listOf(
        ProductEntity(id = 0, name = "Hamburger", buyPrice = 100, sellPrice = 200),
        ProductEntity(id = 1, name = "Pizza", buyPrice = 200, sellPrice = 300),
        ProductEntity(id = 2, name = "Hot Dog", buyPrice = 50, sellPrice = 100),
        ProductEntity(id = 3, name = "Fries", buyPrice = 20, sellPrice = 50),
        ProductEntity(id = 4, name = "Coke", buyPrice = 10, sellPrice = 20),
    )

    override fun getAllProducts(): Flow<List<ProductEntity>> = flowOf(productsList)

    override fun getProductById(id: Int): Flow<ProductEntity> = flowOf(productsList.first { it.id == id })

    override fun getProductByName(name: String): Flow<List<ProductEntity>> = flowOf(productsList.filter { it.name == name })

    override suspend fun insertProduct(product: ProductEntity) {}

    override suspend fun deleteProductById(id: Int) {
    }

}