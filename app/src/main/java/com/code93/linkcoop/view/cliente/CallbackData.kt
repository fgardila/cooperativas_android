package com.code93.linkcoop.view.cliente

import com.code93.linkcoop.persistence.models.DataTransaccion

interface CallbackData {
    fun onClickData(data: DataTransaccion, position: Int)
}