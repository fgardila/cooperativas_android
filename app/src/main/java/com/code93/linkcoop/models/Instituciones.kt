package com.code93.linkcoop.models

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "instituciones_table")
data class Instituciones(
        @PrimaryKey
        var _id: String = "",
        var _namec: String? = "",
        var _product: String? = "",
        var _service: String? = "",
        var _channel: String? = "",
        var url_imagen: String? = "",
        var _transaction : List<Transaction>? = mutableListOf()
) : Parcelable {}