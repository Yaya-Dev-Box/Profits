package com.yayarh.profits.data.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Register")
data class RegisterEntity(
    @PrimaryKey @ColumnInfo("id") val id: Int,
    @ColumnInfo("name") val name: String,
    @ColumnInfo("amount") val amount: Int,
)
