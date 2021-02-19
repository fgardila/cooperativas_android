package com.code93.linkcoop.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Referencias(
        var description: String = "",
        var data_type: String = "",
        var buss_type: String = "",
        var identificator: String = "",
        var mode: String = "",
        var value: String = ""
) : Parcelable { }