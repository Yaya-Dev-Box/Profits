package com.yayarh.profits.data.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import java.time.LocalDate

@Entity(tableName = "DailySales", indices = [Index(value = ["date"], unique = true)])
data class DailySalesEntity(
    @PrimaryKey(autoGenerate = true) @ColumnInfo("id") val id: Int,
    @ColumnInfo("date") val date: LocalDate,
    @ColumnInfo("salesList") val salesList: String,
    @ColumnInfo("totalSales") val totalSales: Int,
    @ColumnInfo("profits") val profits: Int,
)
