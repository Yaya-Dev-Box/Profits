package com.yayarh.profits.data.repos

import com.yayarh.profits.data.daos.ProductsDao
import com.yayarh.profits.data.models.ProductEntity
import javax.inject.Inject

class ProductsRepoImpl @Inject constructor(private val productsDao: ProductsDao) : ProductsRepo {
    override fun getAllProducts() = productsDao.getAllProducts()
    override fun getProductById(id: Int) = productsDao.getProductById(id)
    override fun getProductByName(name: String) = productsDao.getProductByName(name)
    override suspend fun insertProduct(product: ProductEntity) = productsDao.insertProduct(product)
    override suspend fun deleteProductById(id: Int) = productsDao.deleteProductById(id)
}