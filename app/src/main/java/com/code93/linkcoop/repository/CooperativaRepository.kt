package com.code93.linkcoop.repository

import com.code93.linkcoop.data.CooperativaDao
import com.code93.linkcoop.models.Cooperativa

class CooperativaRepository(private val cooperativaDao: CooperativaDao) {

    val realAllData: List<Cooperativa> = cooperativaDao.readAllData()

    suspend fun addCooperativa(user: Cooperativa){
        cooperativaDao.addCooperativa(user)
    }

    suspend fun deleteAllCooperativas() {
        cooperativaDao.deleteAllCooperativas()
    }
}