package com.yayarh.profits.data.repos

import com.yayarh.profits.data.daos.ProductsDao
import com.yayarh.profits.data.models.ProductEntity
import javax.inject.Inject

class ProductsRepo @Inject constructor(private val productsDao: ProductsDao) {
    fun getAllProducts() = productsDao.getAllProducts()
    fun getProductById(id: Int) = productsDao.getProductById(id)
    fun getProductByName(name: String) = productsDao.getProductByName(name)
    suspend fun insertProduct(product: ProductEntity) = productsDao.insertProduct(product)
    suspend fun deleteProductById(id: Int) = productsDao.deleteProductById(id)
}