package com.code93.linkcoop.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.code93.linkcoop.data.LinkCoopDatabase
import com.code93.linkcoop.models.LogTransacciones
import com.code93.linkcoop.repository.LogTransaccionesRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LogTransaccionesViewModel(application: Application): AndroidViewModel(application) {

    val readAllData: LiveData<List<LogTransacciones>>
    private val repository: LogTransaccionesRepository

    init {
        val LogTransaccionesDao = LinkCoopDatabase.getDatabase(application).logTransaccionesDao()
        repository = LogTransaccionesRepository(LogTransaccionesDao)
        readAllData = LogTransaccionesDao.readAllData()
    }

    fun addLogTransacciones(LogTransacciones: LogTransacciones) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.addLogTransacciones(LogTransacciones)
        }
    }


    fun deleteAllLogTransaccioness() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteAllLogTransaccioness()
        }
    }

    fun updateLogTransacciones(LogTransacciones: LogTransacciones) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateLogTransacciones(LogTransacciones)
        }
    }
}