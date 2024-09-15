package com.yayarh.profits.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.yayarh.profits.data.daos.DailySalesDao
import com.yayarh.profits.data.daos.OrdersDao
import com.yayarh.profits.data.daos.ProductsDao
import com.yayarh.profits.data.models.DailySalesEntity
import com.yayarh.profits.data.models.OrderEntity
import com.yayarh.profits.data.models.ProductEntity
import com.yayarh.profits.data.utils.roomConverters.LocalDateConverter

@Database(
    entities = [
        ProductEntity::class,
        DailySalesEntity::class,
        OrderEntity::class
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(LocalDateConverter::class)
abstract class MyDatabase : RoomDatabase() {
    abstract fun productsDao(): ProductsDao
    abstract fun dailySalesDao(): DailySalesDao
    abstract fun ordersDao(): OrdersDao
}