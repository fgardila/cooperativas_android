package com.code93.linkcoop

import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.*

object StringTools {

    fun padleft(s: String, len: Int, c: Char): String? {
        var s = s
        s = s.trim { it <= ' ' }
        if (s.length > len) {
            return null
        }
        val d = StringBuilder(len)
        var fill = len - s.length
        while (fill-- > 0) {
            d.append(c)
        }
        d.append(s)
        return d.toString()
    }

    fun padright(s: String, len: Int, c: Char): String? {
        var s = s
        s = s.trim { it <= ' ' }
        val d = java.lang.StringBuilder(len)
        var fill = len - s.length
        d.append(s)
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