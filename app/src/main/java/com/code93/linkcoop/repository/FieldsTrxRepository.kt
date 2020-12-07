package com.code93.linkcoop.repository

import androidx.lifecycle.LiveData
import com.code93.linkcoop.data.FieldsTrxDao
import com.code93.linkcoop.models.FieldsTrx

class FieldsTrxRepository(private val fieldsTrxDao: FieldsTrxDao) {

    //val realAllData: LiveData<List<FieldsTrx>> = fieldsTrxDao.readAllData()

    suspend fun addFieldTrx(fieldsTrx: FieldsTrx){
        fieldsTrxDao.addFieldTrx(fieldsTrx)
    }

    suspend fun deleteAllFieldsTrx() {
        fieldsTrxDao.deleteAllFieldsTrx()
    }

    suspend fun updateFieldsTrx(cooperativa: FieldsTrx) {
        fieldsTrxDao.updateFieldsTrx(cooperativa)
    }


}