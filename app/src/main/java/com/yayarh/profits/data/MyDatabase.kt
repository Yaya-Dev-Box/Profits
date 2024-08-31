package com.yayarh.profits.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.yayarh.profits.data.daos.DailySalesDao
import com.yayarh.profits.data.daos.ProductsDao
import com.yayarh.profits.data.daos.RegisterDao
import com.yayarh.profits.data.models.DailySalesEntity
import com.yayarh.profits.data.models.ProductEntity
import com.yayarh.profits.data.models.RegisterEntity
import com.yayarh.profits.data.utils.roomConverters.LocalDateConverter

@Database(
    entities = [
        ProductEntity::class,
        RegisterEntity::class,
        DailySalesEntity::class
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(LocalDateConverter::class)
abstract class MyDatabase : RoomDatabase() {
    abstract fun productsDao(): ProductsDao
    abstract fun registerDao(): RegisterDao
    abstract fun dailySalesDao(): DailySalesDao
}