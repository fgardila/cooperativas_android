package com.code93.linkcoop.models

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity()
data class LogTransacciones(
        @PrimaryKey(autoGenerate = true)
        var id: Int = 0,
        var comercio: Comercio = Comercio(),
        var cooperativa: Cooperativa = Cooperativa(),
        var transaction: Transaction = Transaction(),
        var fieldsTrxSend: FieldsTrx = FieldsTrx(),
        var fieldsTrxResponse: FieldsTrx = FieldsTrx()
) : Parcelable {}