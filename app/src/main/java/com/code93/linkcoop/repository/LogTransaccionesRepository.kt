package com.code93.linkcoop.repository

import androidx.lifecycle.LiveData
import com.code93.linkcoop.data.LogTransaccionesDao
import com.code93.linkcoop.models.LogTransacciones

class LogTransaccionesRepository(private val logTransaccionesDao: LogTransaccionesDao) {

    val realAllData: LiveData<List<LogTransacciones>> = logTransaccionesDao.readAllData()

    suspend fun addLogTransacciones(LogTransacciones: LogTransacciones){
        logTransaccionesDao.addLogTransacciones(LogTransacciones)
    }

    suspend fun deleteAllLogTransaccioness() {
        logTransaccionesDao.deleteAllLogTransaccioness()
    }

    suspend fun updateLogTransacciones(LogTransacciones: LogTransacciones) {
        logTransaccionesDao.updateLogTransacciones(LogTransacciones)
    }


}