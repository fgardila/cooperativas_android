package com.code93.linkcoop.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Transaction(
        var _code: String = "",
        var _namet: String = "",
        var _cost: String = ""
) : Parcelable { }
