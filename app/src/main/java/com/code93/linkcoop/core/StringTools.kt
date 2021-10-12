package com.code93.linkcoop.core

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

    @JvmStatic
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

    fun ValidaCedulaRuc(x: String): Boolean {
        var suma = 0
        return if (x.length == 9) {
            false
        } else {
            val a = IntArray(x.length / 2)
            val b = IntArray(x.length / 2)
            var c = 0
            var d = 1
            for (i in 0 until x.length / 2) {
                a[i] = x[c].toString().toInt()
                c = c + 2
                if (i < x.length / 2 - 1) {
                    b[i] = x[d].toString().toInt()
                    d = d + 2
                }
            }
            for (i in a.indices) {
                a[i] = a[i] * 2
                if (a[i] > 9) {
                    a[i] = a[i] - 9
                }
                suma = suma + a[i] + b[i]
            }
            val aux = suma / 10
            val dec = (aux + 1) * 10
            if (dec - suma == x[x.length - 1].toString()
                    .toInt()
            ) true else suma % 10 == 0 && x[x.length - 1] == '0'
        }
    }
}