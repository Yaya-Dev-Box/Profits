package com.yayarh.profits.data.utils.roomConverters

import androidx.room.TypeConverter
import java.time.LocalDate


object LocalDateConverter {

    @TypeConverter
    fun toDate(epochDays: Long?): LocalDate? {
        return try {
            LocalDate.ofEpochDay(epochDays ?: return null)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    @TypeConverter
    fun toDateString(date: LocalDate?): Long? {
        return date?.toEpochDay()
    }

}
