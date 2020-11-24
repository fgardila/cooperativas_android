package com.code93.linkcoop.data

import androidx.lifecycle.LiveData
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.code93.linkcoop.models.Cooperativa

interface CooperativaDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addCooperativa(cooperativa: Cooperativa)

    @Query("DELETE FROM cooperativa_table")
    suspend fun deleteAllCooperativas()

    @Query("SELECT * FROM cooperativa_table")
    fun readAllData(): List<Cooperativa>
}