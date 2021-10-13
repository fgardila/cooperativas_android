package com.code93.linkcoop.core.logger

import com.code93.linkcoop.BuildConfig
import com.orhanobut.logger.AndroidLogAdapter
import com.orhanobut.logger.Logger
import com.orhanobut.logger.PrettyFormatStrategy

class L {
    init {
        val formatStrategy = PrettyFormatStrategy.newBuilder()
            .methodCount(1)
            .methodOffset(6)
            .tag("LinkCoop")
            .build()
        Logger.addLogAdapter(object : AndroidLogAdapter(formatStrategy){
            override fun isLoggable(priority: Int, tag: String?): Boolean {
                return BuildConfig.DEBUG
            }
        })
    }
    companion object {
        private var instance: L? = null

        fun d(message: String) {
            iniciarLog()
            Logger.d(message)
        }

        fun v(message: String) {
            iniciarLog()
            Logger.v(message)
        }

        private fun iniciarLog() {
            if (instance == null) {
                instance = L()
            }
        }
    }
}