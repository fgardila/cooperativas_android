package com.code93.linkcoop.repository

import androidx.lifecycle.LiveData
import com.code93.linkcoop.data.CooperativaDao
import com.code93.linkcoop.models.Cooperativa

class CooperativaRepository(private val cooperativaDao: CooperativaDao) {

    val realAllData: LiveData<List<Cooperativa>> = cooperativaDao.readAllData()

    suspend fun addCooperativa(cooperativa: Cooperativa){
        cooperativaDao.addCooperativa(cooperativa)
    }

    suspend fun deleteAllCooperativas() {
        cooperativaDao.deleteAllCooperativas()
    }

    suspend fun updateCooperativa(cooperativa: Cooperativa) {
        cooperativaDao.updateCooperativa(cooperativa)
    }


}