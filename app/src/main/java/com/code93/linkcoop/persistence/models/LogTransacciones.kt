package com.code93.linkcoop.persistence.models

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity()
data class LogTransacciones(
        @PrimaryKey(autoGenerate = true)
        var id: Int,
        var comercio: Comercio,
        var cooperativa: Cooperativa,
        var transaction: Transaction,
        var fieldsTrxSend : FieldsTrx,
        var fieldsTrxResponse : FieldsTrx
) : Parcelable {}