package com.code93.linkcoop.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.code93.linkcoop.persistence.data.LinkCoopDatabase
import com.code93.linkcoop.persistence.models.FieldsTrx
import com.code93.linkcoop.persistence.repository.FieldsTrxRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FieldsTrxViewModel(application: Application): AndroidViewModel(application) {

    //val readAllData: LiveData<List<FieldsTrx>>
    private val repository: FieldsTrxRepository

    init {
        val fieldsTrxDao = LinkCoopDatabase.getDatabase(application).fieldsTrxDao()
        repository = FieldsTrxRepository(fieldsTrxDao)
        //readAllData = fieldsTrxDao.readAllData()
    }

    fun addFieldTrx(fieldsTrx: FieldsTrx) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.addFieldTrx(fieldsTrx)
        }
    }


    fun deleteAllFieldsTrx() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteAllFieldsTrx()
        }
    }

    fun updateFieldsTrx(fieldsTrx: FieldsTrx) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateFieldsTrx(fieldsTrx)
        }
    }
}