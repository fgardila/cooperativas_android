package com.code93.linkcoop.models

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize


@Parcelize
@Entity(tableName = "autorizadores_table")
data class Autorizadores(
        @PrimaryKey
        var _id: String = "",
        var _namec: String = "",
) : Parcelable {}