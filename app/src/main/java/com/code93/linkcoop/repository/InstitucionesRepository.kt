package com.code93.linkcoop.repository

import androidx.lifecycle.LiveData
import com.code93.linkcoop.data.InstitucionesDao
import com.code93.linkcoop.models.Instituciones

class InstitucionesRepository(private val institucionesDao: InstitucionesDao) {

    val realAllData: LiveData<List<Instituciones>> = institucionesDao.readAllData()

    suspend fun addInstituciones(instituciones: Instituciones){
        institucionesDao.addInstituciones(instituciones)
    }

    suspend fun deleteAllInstituciones() {
        institucionesDao.deleteAllInstituciones()
    }

    suspend fun updateInstituciones(instituciones: Instituciones) {
        institucionesDao.updateInstituciones(instituciones)
    }


}