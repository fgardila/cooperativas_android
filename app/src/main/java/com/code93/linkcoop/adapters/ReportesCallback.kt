package com.code93.linkcoop.adapters

import com.code93.linkcoop.persistence.models.LogTransacciones

interface ReportesCallback {
    fun onReporteCallback(imprimir: Boolean, value: LogTransacciones)
}