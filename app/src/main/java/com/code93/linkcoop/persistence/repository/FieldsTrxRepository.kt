package com.code93.linkcoop.persistence.repository

import com.code93.linkcoop.persistence.data.FieldsTrxDao
import com.code93.linkcoop.persistence.models.FieldsTrx

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