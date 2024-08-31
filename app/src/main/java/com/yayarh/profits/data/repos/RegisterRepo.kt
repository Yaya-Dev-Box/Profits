package com.yayarh.profits.data.repos

import com.yayarh.profits.data.models.RegisterEntity
import kotlinx.coroutines.flow.Flow

interface RegisterRepo {

    fun getRegisterItems(): Flow<List<RegisterEntity>>

    suspend fun insertRegisterItem(registerEntity: RegisterEntity)

    suspend fun deleteRegisterItem(id: Int)

    /** @param amount is the amount to ADD or SUBTRACT */
    suspend fun updateRegisterItemAmount(id: Int, amount: Int)

    suspend fun clearRegister()
}