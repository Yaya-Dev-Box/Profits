package com.yayarh.profits.data.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.yayarh.profits.data.models.ProductEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ProductsDao {

    @Query("SELECT * FROM Products")
    fun getAllProducts(): Flow<List<ProductEntity>>

    @Query("SELECT * FROM Products WHERE id = :id LIMIT 1")
    fun getProductById(id: Int): Flow<ProductEntity>

    @Query("SELECT * FROM Products WHERE name LIKE :name")
    fun getProductByName(name: String): Flow<List<ProductEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProduct(product: ProductEntity)

    @Query("DELETE FROM Products WHERE id = :id")
    suspend fun deleteProductById(id: Int)

}