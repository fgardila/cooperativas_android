package com.code93.linkcoop

import android.app.Application
import com.code93.linkcoop.persistence.cache.SP2.Companion.getInstance
import com.code93.linkcoop.persistence.cache.SP2.Companion.net_direccionip
import com.code93.linkcoop.persistence.cache.SP2.Companion.net_puerto
import com.code93.linkcoop.persistence.cache.SP2.Companion.aes_iv
import com.code93.linkcoop.persistence.cache.SP2.Companion.aes_password
import com.code93.linkcoop.persistence.cache.SP2.Companion.aes_salt
import android.os.Build
import com.zcs.sdk.card.CardInfoEntity
import com.code93.linkcoop.persistence.cache.SP2
import com.zcs.sdk.DriverManager

class MyApp : Application() {

    companion object {
        @JvmField
        var sDriverManager: DriverManager? = null
        @JvmField
        var sp2: SP2? = null
    }

    override fun onCreate() {
        super.onCreate()
        if (Build.MODEL == "Z90") {
            sDriverManager = DriverManager.getInstance()
            Config.init(this)
        }
        sp2 = getInstance(this)
        if (sp2 != null) {
            val net_direccionip = sp2!!.getString(net_direccionip, null)
            if (net_direccionip == null) {
                sp2!!.putString(SP2.net_direccionip, getString(R.string.net_direccionip))
            }
            val net_puerto = sp2!!.getString(net_puerto, null)
            if (net_puerto == null) {
                sp2!!.putString(SP2.net_puerto, getString(R.string.net_puerto))
            }
            val aes_iv = sp2!!.getString(aes_iv, null)
            if (aes_iv == null) {
                sp2!!.putString(SP2.aes_iv, getString(R.string.aes_iv))
            }
            val aes_password = sp2!!.getString(aes_password, null)
            if (aes_password == null) {
                sp2!!.putString(SP2.aes_password, getString(R.string.aes_password))
            }
            val aes_salt = sp2!!.getString(aes_salt, null)
            if (aes_salt == null) {
                sp2!!.putString(SP2.aes_salt, getString(R.string.aes_salt))
            }
        }
    }
}