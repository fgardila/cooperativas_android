package com.code93.juliopaprika_menuvirtual.data

import androidx.room.TypeConverter
import com.code93.linkcoop.persistence.models.Comercio
import com.code93.linkcoop.persistence.models.Cooperativa
import com.code93.linkcoop.persistence.models.FieldsTrx
import com.code93.linkcoop.persistence.models.Transaction
import com.google.gson.Gson

class Converters {

    @TypeConverter
    fun listToJson(value: List<Transaction>?) = Gson().toJson(value)

    @TypeConverter
    fun jsonToList(value: String) = Gson().fromJson(value, Array<Transaction>::class.java).toList()

    @TypeConverter
    fun comercioToJson(value: Comercio?) = Gson().toJson(value)

    @TypeConverter
    fun jsonToComercio(value: String) = Gson().fromJson(value, Comercio::class.java)

    @TypeConverter
    fun fieldsTrxToJson(value: FieldsTrx?) = Gson().toJson(value)

    @TypeConverter
    fun jsonToFieldsTrx(value: String) = Gson().fromJson(value, FieldsTrx::class.java)

    @TypeConverter
    fun cooperativaToJson(value: Cooperativa?) = Gson().toJson(value)

    @TypeConverter
    fun jsonToCooperativa(value: String) = Gson().fromJson(value, Cooperativa::class.java)

    @TypeConverter
    fun transactionToJson(value: Transaction?) = Gson().toJson(value)

    @TypeConverter
    fun jsonToTransaction(value: String) = Gson().fromJson(value, Transaction::class.java)

}