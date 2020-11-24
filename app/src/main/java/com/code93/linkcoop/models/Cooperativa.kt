package com.code93.linkcoop.models

import android.os.Parcelable
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "cooperativa_table")
data class Cooperativa(
        @PrimaryKey
        var _id: String = "",
        var _namec: String = "",
        var _transaction : List<Transaction>
) : Parcelable {}