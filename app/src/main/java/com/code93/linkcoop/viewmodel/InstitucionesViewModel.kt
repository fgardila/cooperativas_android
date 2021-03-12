package com.code93.linkcoop.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.code93.linkcoop.data.LinkCoopDatabase
import com.code93.linkcoop.models.Instituciones
import com.code93.linkcoop.repository.InstitucionesRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class InstitucionesViewModel(application: Application): AndroidViewModel(application) {

    val readAllData: LiveData<List<Instituciones>>
    private val repository: InstitucionesRepository

    init {
        val institucionesDao = LinkCoopDatabase.getDatabase(application).institucionesDao()
        repository = InstitucionesRepository(institucionesDao)
        readAllData = institucionesDao.readAllData()
    }

    fun addInstituciones(instituciones: Instituciones) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.addInstituciones(instituciones)
        }
    }


    fun deleteAllInstituciones() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteAllInstituciones()
        }
    }

    fun updateInstituciones(instituciones: Instituciones) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateInstituciones(instituciones)
        }
    }
}