package com.code93.linkcoop.persistence.data

import androidx.lifecycle.LiveData
import androidx.room.*
import com.code93.linkcoop.persistence.models.LogTransacciones

@Dao
interface LogTransaccionesDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addLogTransacciones(LogTransacciones: LogTransacciones)

    @Query("DELETE FROM LogTransacciones")
    suspend fun deleteAllLogTransaccioness()

    @Query("SELECT * FROM LogTransacciones")
    fun readAllData(): LiveData<List<LogTransacciones>>

    @Update
    fun updateLogTransacciones(LogTransacciones: LogTransacciones)
}