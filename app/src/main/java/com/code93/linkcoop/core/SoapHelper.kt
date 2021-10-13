package com.code93.linkcoop.core

import org.ksoap2.serialization.SoapObject

object SoapHelper {

    val METHOD_NAME = "Invoque_Async_Service"
    val NAMESPACE = "http://tempuri.org/"

    fun getSoapObject(): SoapObject {
        return SoapObject(NAMESPACE, METHOD_NAME)
    }

}