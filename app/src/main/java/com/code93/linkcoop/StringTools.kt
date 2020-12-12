package com.code93.linkcoop

import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.*

object StringTools {

    fun padleft(s: String, len: Int, c: Char): String? {
        var data = s
        data = data.trim { it <= ' ' }
        if (data.length > len) {
            return null
        }
        val d = StringBuilder(len)
        var fill = len - data.length
        while (fill-- > 0) {
            d.append(c)
        }
        d.append(data)
        return d.toString()
    }

    fun padright(s: String, len: Int, c: Char): String {
        var data = s
        data = data.trim { it <= ' ' }
        val d = java.lang.StringBuilder(len)
        var fill = len - data.length
        d.append(data)
        while (fill-- > 0) d.append(c)
        return d.toString()
    }

    fun formatoMonedaUSD(amount: String): String {
        val df = DecimalFormat("#,###.##", DecimalFormatSymbols(Locale.ITALIAN))
        var d = 0.0
        if (!isNullWithTrim(amount)) {
            d = amount.toDouble()
        }
        return "$" + df.format(d)
    }

    fun formatoMonedaCOP(amount: String): String {
        val df = DecimalFormat("#,###", DecimalFormatSymbols(Locale.ITALIAN))
        var d = 0.0
        if (!isNullWithTrim(amount)) {
            d = amount.toDouble()
        }
        return "$" + df.format(d)
    }

    fun isNullWithTrim(str: String?): Boolean {
        return str == null || str.trim { it <= ' ' } == "" || str.trim { it <= ' ' } == "null"
    }
}