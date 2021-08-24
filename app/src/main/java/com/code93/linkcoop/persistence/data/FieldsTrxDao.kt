package com.code93.linkcoop.persistence.data

import androidx.room.*
import com.code93.linkcoop.persistence.models.FieldsTrx

@Dao
interface FieldsTrxDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addFieldTrx(fieldsTrx: FieldsTrx)

    @Query("DELETE FROM FieldsTrx")
    suspend fun deleteAllFieldsTrx()

    @Update
    fun updateFieldsTrx(fieldsTrx: FieldsTrx)
}