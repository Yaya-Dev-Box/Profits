package com.yayarh.profits.data.repos

import com.yayarh.profits.data.daos.RegisterDao
import com.yayarh.profits.data.models.RegisterEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class RegisterRepoImpl @Inject constructor(private val registerDao: RegisterDao) : RegisterRepo {

    override fun getRegisterItems(): Flow<List<RegisterEntity>> = registerDao.getRegisterItems()

    override suspend fun insertRegisterItem(registerEntity: RegisterEntity) = registerDao.insertRegisterItem(registerEntity)

    override suspend fun deleteRegisterItem(id: Int) = registerDao.deleteRegisterItem(id)

    override suspend fun updateRegisterItemAmount(id: Int, amount: Int) = registerDao.updateRegisterItemAmount(id, amount)

    override suspend fun clearRegister() = registerDao.deleteAll()

}