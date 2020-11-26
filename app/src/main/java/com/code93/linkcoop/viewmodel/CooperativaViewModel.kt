package com.code93.linkcoop.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.code93.linkcoop.data.CooperativaDatabase
import com.code93.linkcoop.models.Cooperativa
import com.code93.linkcoop.repository.CooperativaRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CooperativaViewModel(application: Application): AndroidViewModel(application) {

    val readAllData: LiveData<List<Cooperativa>>
    private val repository: CooperativaRepository

    init {
        val cooperativaDao = CooperativaDatabase.getDatabase(application).cooperativaDao()
        repository = CooperativaRepository(cooperativaDao)
        readAllData = cooperativaDao.readAllData()
    }

    fun addCooperativa(cooperativa: Cooperativa) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.addCooperativa(cooperativa)
        }
    }


    fun deleteAllCooperativas() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteAllCooperativas()
        }
    }

    fun updateCooperativa(cooperativa: Cooperativa) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateCooperativa(cooperativa)
        }
    }
}