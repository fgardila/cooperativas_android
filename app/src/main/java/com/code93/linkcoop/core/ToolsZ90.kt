package com.code93.linkcoop.core

import android.os.Build

object ToolsZ90 {

    @JvmStatic
    fun getSn(): String {
        return if (Build.MODEL.contains("Z90")) {
            try {
                val c = Class.forName("android.os.SystemProperties")
                val get = c.getMethod("get", String::class.java)
                get.invoke(c, "ro.serialno") as String
            } catch (ignored: Exception) {
                ""
            }
        } else {
            ""
        }
    }

    @JvmStatic
    fun isZ90(): Boolean {
        return Build.MODEL.contains("Z90")
    }
}