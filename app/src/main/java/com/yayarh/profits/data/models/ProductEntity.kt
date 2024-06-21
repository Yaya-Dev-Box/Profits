package com.yayarh.profits.data.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Products")
data class ProductEntity(
    @PrimaryKey(autoGenerate = true) val id: Int,
    @ColumnInfo("name") val name: String,
    @ColumnInfo("buyPrice") val buyPrice: Int,
    @ColumnInfo("sellPrice") val sellPrice: Int,
)
