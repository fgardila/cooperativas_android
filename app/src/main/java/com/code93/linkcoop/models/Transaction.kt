package com.code93.linkcoop.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Transaction(
        var _bitmap: String? = "",
        var _message_code: String? = "",
        var _code: String? = "",
        var _namet: String? = "",
        var _cost: String? = "",
        var _subservice: String? = "",
        var referencias: List<Referencias>?
) : Parcelable { }
