package com.code93.linkcoop.models

import android.os.Parcelable
import androidx.room.Entity
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "cooperativa_table")
data class Cooperativa(
        var _id: String = "",
        var _namec: String = "",
        var _transaction : String = ""
) : Parcelable {}