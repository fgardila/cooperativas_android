package com.code93.linkcoop.data

import androidx.lifecycle.LiveData
import androidx.room.*
import com.code93.linkcoop.models.Instituciones

@Dao
interface InstitucionesDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addInstituciones(instituciones: Instituciones)

    @Query("DELETE FROM instituciones_table")
    suspend fun deleteAllInstituciones()

    @Query("SELECT * FROM instituciones_table")
    fun readAllData(): LiveData<List<Instituciones>>

    @Update
    fun updateInstituciones(instituciones: Instituciones)
}