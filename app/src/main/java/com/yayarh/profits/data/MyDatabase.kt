package com.yayarh.profits.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.yayarh.profits.data.daos.ProductsDao
import com.yayarh.profits.data.models.ProductEntity

@Database(entities = [ProductEntity::class], version = 1)
abstract class MyDatabase : RoomDatabase() {
    abstract fun productsDao(): ProductsDao
}