package com.yayarh.profits.data.repos.base

import com.yayarh.profits.data.models.ProductEntity
import kotlinx.coroutines.flow.Flow

interface ProductsRepo {
    fun getAllProducts(): Flow<List<ProductEntity>>
    fun getProductById(id: Int): Flow<ProductEntity>
    fun getProductByName(name: String): Flow<List<ProductEntity>>
    suspend fun insertProduct(product: ProductEntity)
    suspend fun deleteProductById(id: Int)
}