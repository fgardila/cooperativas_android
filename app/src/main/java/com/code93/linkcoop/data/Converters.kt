package com.code93.juliopaprika_menuvirtual.data

import androidx.room.TypeConverter
import com.code93.linkcoop.models.Transaction
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class Converters {

    @TypeConverter
    fun listToJson(value: List<Transaction>?) = Gson().toJson(value)

    @TypeConverter
    fun jsonToList(value: String) = Gson().fromJson(value, Array<Transaction>::class.java).toList()

}