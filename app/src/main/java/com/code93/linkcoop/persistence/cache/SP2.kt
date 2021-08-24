package com.code93.linkcoop.persistence.cache

import android.content.Context
import android.content.SharedPreferences
import android.text.TextUtils

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

        val user = "user"
        val user_encript = "user_encript"
        val pwd = "pwd"
        val saveLogin = "saveLogin"
        val fechaUltimoLogin = "fechaUltimoLogin"
        val SP_LOGIN = "login"
        val SP_TRACE = "trace"
        val aes_iv = "aes_iv"
        val aes_password = "aes_password"
        val aes_salt = "aes_salt"
        val net_direccionip = "net_direccionip"
        val net_puerto = "net_puerto"
        val comercio_nombre = "comercio_nombre"
        val comercio_ruc = "comercio_ruc"
        val comercio_direccion = "comercio_direccion"

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