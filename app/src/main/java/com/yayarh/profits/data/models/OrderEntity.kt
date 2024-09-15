package com.yayarh.profits.data.models

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Orders")
data class OrderEntity(
    /** The id of this specific item, required by SQLite */
    @PrimaryKey(autoGenerate = true) @ColumnInfo("id") val id: Int,

    @ColumnInfo("quantity") val quantity: Int,

    /** The id of the order this item belongs to, the items which have the same [orderId] all belong together */
    @ColumnInfo("orderId") val orderId: Int,

    @Embedded("embeddedProduct_") val product: ProductEntity,
)
