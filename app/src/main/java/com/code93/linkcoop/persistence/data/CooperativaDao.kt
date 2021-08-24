package com.code93.linkcoop.persistence.data

import androidx.lifecycle.LiveData
import androidx.room.*
import com.code93.linkcoop.persistence.models.Cooperativa

@Dao
interface CooperativaDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addCooperativa(cooperativa: Cooperativa)

    @Query("DELETE FROM cooperativa_table")
    suspend fun deleteAllCooperativas()

    @Query("SELECT * FROM cooperativa_table")
    fun readAllData(): LiveData<List<Cooperativa>>

    @Update
    fun updateCooperativa(cooperativa: Cooperativa)
}