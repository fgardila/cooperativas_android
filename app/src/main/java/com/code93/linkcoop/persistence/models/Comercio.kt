package com.code93.linkcoop.persistence.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Comercio(
        var nombre: String = "",
        var ruc: String = "",
        var direccion: String = ""
) : Parcelable { }