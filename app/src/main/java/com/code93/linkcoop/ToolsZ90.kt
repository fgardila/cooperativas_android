package com.code93.linkcoop

import android.content.Context
import android.os.Build

object ToolsZ90 {

    @JvmStatic
    fun getSn(ctx: Context?): String {
        var serial: String? = null
        try {
            val c = Class.forName("android.os.SystemProperties")
            val get = c.getMethod("get", String::class.java)
            serial = get.invoke(c, "ro.serialno") as String
        } catch (ignored: Exception) {
            return "";
        }
        return serial
    }

    @JvmStatic
    fun isZ90(): Boolean {
        return Build.MODEL.contains("Z90")
    }
}