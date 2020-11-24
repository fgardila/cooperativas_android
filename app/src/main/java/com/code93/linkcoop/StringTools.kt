package com.code93.linkcoop

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
}