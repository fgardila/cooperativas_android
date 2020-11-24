package com.code93.linkcoop.cache

import android.content.Context
import android.content.SharedPreferences
import android.text.TextUtils
import com.code93.linkcoop.StringTools

/**
 * Created by wanglei on 2016/11/27.
 */
class SP2 private constructor(context: Context) : ICache {

    override fun remove(key: String?) {
        editor.remove(key)
        editor.apply()
    }

    override fun contains(key: String?): Boolean {
        return sharedPreferences.contains(key)
    }

    override fun clear() {
        editor.clear().apply()
    }

    fun putInt(key: String?, value: Int) {
        editor.putInt(key, value)
        editor.apply()
    }

    fun getInt(key: String?, defValue: Int): Int {
        return sharedPreferences.getInt(key, defValue)
    }

    fun putLong(key: String?, value: Long?) {
        editor.putLong(key, value!!)
        editor.apply()
    }

    fun getLong(key: String?, defValue: Long): Long {
        return sharedPreferences.getLong(key, defValue)
    }

    fun putBoolean(key: String?, value: Boolean?) {
        editor.putBoolean(key, value!!)
        editor.apply()
    }

    fun getBoolean(key: String?, defValue: Boolean): Boolean {
        return sharedPreferences.getBoolean(key, defValue)
    }

    fun putString(key: String?, value: String?) {
        editor.putString(key, value)
        editor.apply()
    }

    fun getString(key: String?, defValue: String?): String? {
        return sharedPreferences.getString(key, defValue)
    }

    override fun get(key: String?): Any? {
        return null
    }

    override fun put(key: String?, value: Any?) {}

    fun getTraceNo() : String? {
        var traceNoInt = getInt(SP_TRACE, 0)
        if (traceNoInt == 0) {
            traceNoInt = 1
        }
        //return StringTools.padleft(traceNoInt.toString(), 6, '0')!!
        return traceNoInt.toString()
    }

    fun incTraceNo() {
        var traceNoInt = getInt(SP_TRACE, 0)
        if (traceNoInt == 999999) {
            traceNoInt = 0
        }
        traceNoInt += 1
        putInt(SP_TRACE, traceNoInt)
    }

    companion object {

        val SP_LOGIN = "login"
        val SP_TRACE = "trace"

        private lateinit var sharedPreferences: SharedPreferences
        private lateinit var editor: SharedPreferences.Editor
        private var SP_NAME = "config"
        private var instance: SP2? = null
        fun init(spName: String) {
            if (!TextUtils.isEmpty(spName)) SP_NAME = spName
        }

        fun getInstance(context: Context): SP2? {
            if (instance == null) {
                synchronized(SP2::class.java) {
                    if (instance == null) {
                        instance = SP2(context.applicationContext)
                    }
                }
            }
            return instance
        }
    }

    init {
        sharedPreferences = context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE)
        editor = sharedPreferences.edit()
    }
}