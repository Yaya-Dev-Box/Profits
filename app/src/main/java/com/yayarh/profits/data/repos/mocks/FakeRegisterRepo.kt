package com.yayarh.profits.data.repos.mocks

import com.yayarh.profits.data.models.RegisterEntity
import com.yayarh.profits.data.repos.RegisterRepo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class FakeRegisterRepo : RegisterRepo {

    private val fakeList = listOf(
        RegisterEntity(1, "Burger", 10),
        RegisterEntity(2, "Pizza", 5),
        RegisterEntity(3, "Coke", 2),
        RegisterEntity(4, "Fries", 3),
    )

    override fun getRegisterItems(): Flow<List<RegisterEntity>> = flowOf(fakeList)

    override suspend fun insertRegisterItem(registerEntity: RegisterEntity) = Unit

    override suspend fun deleteRegisterItem(id: Int) = Unit

    override suspend fun updateRegisterItemAmount(id: Int, amount: Int) = Unit

    override suspend fun clearRegister() = Unit

}