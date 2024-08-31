package com.yayarh.profits.data.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.yayarh.profits.data.models.RegisterEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface RegisterDao {

    @Query("SELECT * FROM Register")
    fun getRegisterItems(): Flow<List<RegisterEntity>>

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertRegisterItem(registerEntity: RegisterEntity)

    @Query("DELETE FROM Register WHERE id = :id")
    suspend fun deleteRegisterItem(id: Int)

    @Query("UPDATE Register SET amount = amount + :amount  WHERE id = :id")
    suspend fun updateRegisterItemAmount(id: Int, amount: Int)

    @Query("DELETE FROM Register")
    suspend fun deleteAll()
}